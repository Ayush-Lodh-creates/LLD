package job_scheduler.services;

import job_scheduler.entities.Job;
import job_scheduler.enums.Priority;
import job_scheduler.enums.Status;
import job_scheduler.enums.TaskType;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

public class JobScheduler {

    private class ReadyJob implements Runnable, Comparable<ReadyJob> {

        private final Job job;

        public ReadyJob(Job job) {
            this.job = job;
        }

        @Override
        public int compareTo(ReadyJob other) {
            int priorityOrder = Integer.compare(other.job.getPriority().value, job.getPriority().value);
            return priorityOrder != 0 ? priorityOrder : Integer.compare(job.getOrder(), other.job.getOrder());
        }

        @Override
        public void run() {
            runJob(job);
        }
    }

    private final DelayQueue<Job> jobQueue = new DelayQueue<>();
    private final Map<String, Job> jobMap = new ConcurrentHashMap<>();
    private final ExecutorService movers = Executors.newSingleThreadExecutor();
    private final ThreadPoolExecutor workers;
    private boolean shutdown;

    public JobScheduler(int workerCount) {
        workers = new ThreadPoolExecutor(workerCount, workerCount, 0L,
                TimeUnit.MILLISECONDS, new PriorityBlockingQueue<>());
        movers.execute(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Job job = jobQueue.take();
                    synchronized (this) {
                        if (shutdown) return;
                        if (job.getStatus().get() == Status.SCHEDULED) {
                            workers.execute(new ReadyJob(job));
                        }
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    public String scheduleOnce(Runnable task, long delay, Priority priority) {
        return schedule(task, delay, priority, TaskType.RUN_ONCE, 0L);
    }

    public String scheduleRecurring(Runnable task, long delay, Priority priority, long interval) {
        if (interval <= 0) {
            throw new IllegalArgumentException("Interval must be positive for recurring jobs.");
        }
        return schedule(task, delay, priority, TaskType.RECURRING, interval);
    }

    private synchronized String schedule(Runnable task, long delay, Priority priority,
                                         TaskType type, long interval) {
        if (shutdown) throw new RejectedExecutionException("Scheduler has been shut down.");
        Objects.requireNonNull(task, "Task is required");
        Objects.requireNonNull(priority, "Priority is required");
        if (delay < 0) throw new IllegalArgumentException("Delay cannot be negative.");

        Job job = new Job(task, priority, type, System.currentTimeMillis() + delay, interval);
        jobMap.put(job.getId(), job);
        jobQueue.offer(job);
        return job.getId();
    }

    public Status getStatus(String id) {
        Job job = jobMap.get(id);
        if (job == null) throw new IllegalArgumentException("Job not found.");
        return job.getStatus().get();
    }

    // A running task may finish, but cancellation prevents any future executions.
    public synchronized boolean cancel(String id) {
        Job job = jobMap.get(id);
        if (job == null) return false;
        while (true) {
            Status status = job.getStatus().get();
            if (status != Status.SCHEDULED && status != Status.RUNNING) return false;
            if (job.getStatus().compareAndSet(status, Status.CANCELLED)) {
                jobQueue.remove(job);
                return true;
            }
        }
    }

    private void runJob(Job job) {
        if (!job.getStatus().compareAndSet(Status.SCHEDULED, Status.RUNNING)) return;
        boolean success = true;
        try {
            job.getTask().run();
        } catch (Exception e) {
            success = false;
            System.err.println("Job with ID: " + job.getId() + " failed: " + e.getMessage());
        }

        synchronized (this) {
            if (job.getStatus().get() == Status.CANCELLED) return;
            if (!success) {
                job.getStatus().compareAndSet(Status.RUNNING, Status.FAILED);
            } else if (job.isRecurring() && !shutdown) {
                // Reuse the same job so status queries and cancellation keep working.
                job.setRunAt(System.currentTimeMillis() + job.getIntervalDelay());
                if (job.getStatus().compareAndSet(Status.RUNNING, Status.SCHEDULED)) {
                    jobQueue.offer(job);
                }
            } else {
                job.getStatus().compareAndSet(Status.RUNNING, Status.SUCCESS);
            }
        }
    }

    public synchronized void shutdown() {
        if (shutdown) return;
        shutdown = true;
        for (Job job : jobMap.values()) cancel(job.getId());
        jobQueue.clear();
        movers.shutdownNow();
        workers.shutdown();
    }
}
