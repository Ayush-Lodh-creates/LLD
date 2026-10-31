package job_scheduler.services;

import job_scheduler.entities.Job;
import job_scheduler.enums.Priority;
import job_scheduler.enums.Status;
import job_scheduler.enums.TaskType;

import java.util.Map;
import java.util.concurrent.*;

public class JobScheduler {

    private class ReadyJob implements Runnable, Comparable<ReadyJob>{

        private final Job job;

        public ReadyJob(Job job) {
            this.job = job;
        }

        @Override
        public int compareTo(ReadyJob o) {
            if(this.job.getPriority().value == o.job.getPriority().value) {
                return Integer.compare(o.job.getOrder(), this.job.getOrder());
            }
            return Integer.compare(o.job.getPriority().value, this.job.getPriority().value);
        }

        @Override
        public void run() {
            runJob(this.job);
        }
    }

    private final DelayQueue<Job> jobQueue = new DelayQueue<>();
    private final Map<String, Job> jobMap = new ConcurrentHashMap<>();
    private final ExecutorService movers = Executors.newSingleThreadExecutor();
    private final ThreadPoolExecutor workers;

    public JobScheduler(int workerCount) {
        workers = new ThreadPoolExecutor(workerCount, workerCount, 0L, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<>());
        movers.execute(() -> {
            try {
                while (true) {
                    Job job = jobQueue.take();
                    workers.execute(new ReadyJob(job));
                }
            } catch (Exception e) {
                throw new RuntimeException("The mover thread interrupted unexpectedly." + e.getMessage());
            }
        });
    }

    public String scheduleOnce(Runnable task, long delay, Priority priority) {
        Job job = new Job(task, priority, TaskType.RUN_ONCE, System.currentTimeMillis() + delay, 0L);
        jobQueue.offer(job);
        jobMap.put(job.getId(), job);
        return job.getId();
    }

    public String scheduleRecurring(Runnable task, long delay, Priority priority, long interval) {
        if(interval < 0) {
            throw new IllegalArgumentException("Interval cannot be negative for recurring jobs.");
        }
        Job job = new Job(task, priority, TaskType.RECURRING, System.currentTimeMillis() + delay, interval);
        jobQueue.offer(job);
        jobMap.put(job.getId(), job);
        return job.getId();
    }

    public Status getStatus(String id) {
        Job job = jobMap.get(id);
        if (job == null) {
            throw new IllegalArgumentException("Job not found.");
        }
        return job.getStatus().get();
    }

    public boolean cancel(String id) {
        Job job = jobMap.get(id);
        if(job == null) {
            return false;
        }
        try {
            if(job.getStatus().get() == Status.CANCELLED || job.getStatus().get() == Status.FAILED) {
                return false;
            }
            job.getStatus().set(Status.CANCELLED);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Exception happened while cancelling Job : " + e.getMessage());
        }
    }

    public void runJob(Job job) {
        if(job.getStatus().get() == Status.CANCELLED || job.getStatus().get() == Status.FAILED) return;
        boolean success = true;
        try {
            job.getStatus().set(Status.RUNNING);
            job.getTask().run();
        } catch (Exception e) {
            success = false;
        }
        if(!success) {
            job.getStatus().set(Status.FAILED);
            System.out.println("Job with ID: " + job.getId() + " failed to execute.");
        } else {
            job.getStatus().set(Status.SUCCESS);
            System.out.println("Job with ID: " + job.getId() + " executed successfully.");
        }
        if(job.isRecurring() && job.getStatus().get() != Status.CANCELLED) {
            jobQueue.offer(new Job(job.getTask(), job.getPriority(), TaskType.RECURRING, System.currentTimeMillis() + job.getIntervalDelay(), job.getIntervalDelay()));
        }
    }

    public void shutdown() {
        movers.shutdownNow();
        workers.shutdown();
    }
}
