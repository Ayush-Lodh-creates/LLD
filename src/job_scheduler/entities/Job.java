package job_scheduler.entities;

import job_scheduler.enums.Priority;
import job_scheduler.enums.Status;
import job_scheduler.enums.TaskType;

import java.util.UUID;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Job implements Delayed {

    private static final AtomicInteger orderCounter = new AtomicInteger(1);

    private String id = UUID.randomUUID().toString();
    private final Runnable task;
    private final Priority priority;
    private final TaskType taskType;
    private Long runAt;
    private Long intervalDelay;
    private Integer order = orderCounter.getAndIncrement();

    private final AtomicReference<Status> status = new AtomicReference<>(Status.SCHEDULED);

    public Job(Runnable task, Priority priority, TaskType taskType, Long runAt, Long intervalDelay) {
        this.task = task;
        this.priority = priority;
        this.taskType = taskType;
        this.runAt = runAt;
        this.intervalDelay = intervalDelay;
    }

    public String getId() {
        return id;
    }

    public Runnable getTask() {
        return task;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public Priority getPriority() {
        return priority;
    }

    public Integer getOrder() {
        return order;
    }

    public Long getRunAt() {
        return runAt;
    }

    public AtomicReference<Status> getStatus() {
        return status;
    }

    public boolean isRecurring() {
        return this.taskType == TaskType.RECURRING;
    }

    public Long getIntervalDelay() {
        return intervalDelay;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return Math.max(0, runAt - System.currentTimeMillis());
    }

    @Override
    public int compareTo(Delayed o) {
        return Long.compare(this.runAt, ((Job) o).runAt);
    }
}
