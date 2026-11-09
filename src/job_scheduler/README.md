# Job Scheduler

This is an in-memory scheduler for delayed one-off tasks and recurring tasks. It separates two questions: when is a job eligible to run, and which eligible job should a worker pick next?

## Supported behavior

- Schedule a `Runnable` after a delay, with low, medium, or high priority.
- Repeat a task after a fixed delay from its previous successful completion.
- Query status using the job ID and cancel pending or running jobs.
- Shut down scheduling while allowing already-running task code to finish.

All delays and recurring intervals passed to the scheduler are in milliseconds.

## Design

[`Job`](entities/Job.java) implements `Delayed` and stores its scheduled time, priority, task, and atomic status. A `DelayQueue<Job>` holds jobs until they become eligible.

[`JobScheduler`](services/JobScheduler.java) starts one mover thread. It blocks on the delay queue, then submits eligible jobs to a `ThreadPoolExecutor`. The executor uses a `PriorityBlockingQueue` of `ReadyJob` wrappers. Higher priority wins among queued work; equal priorities use the job's original creation order.

This separation avoids making a worker wait for a future job while other work could run. Priority applies to work waiting in the ready queue. It does not preempt a running task or guarantee a global start order: the executor can hand initial tasks directly to newly created workers.

## Job lifecycle

```text
SCHEDULED -> RUNNING -> SUCCESS          one-off task
                    -> SCHEDULED        successful recurring task
                    -> FAILED           task throws an Exception
SCHEDULED or RUNNING -> CANCELLED
```

Before execution, a compare-and-set changes `SCHEDULED` to `RUNNING`. A cancelled job that is still in the ready queue therefore becomes a no-op when picked up.

A recurring job is reinserted with the same ID after it finishes. Its next time is completion time plus the interval, so this is fixed-delay scheduling. A slow run shifts later runs; the implementation does not try to catch up to a fixed-rate schedule.

Cancellation changes status and prevents future executions. It does not interrupt a task that has already started. Shutdown rejects new schedules, cancels active job records, clears delayed work, stops the mover, and shuts down the worker executor.

## Trade-offs and remaining work

- Jobs and statuses disappear when the process exits. There is no durable storage, retry policy, or recovery.
- Queue sizes and retained job history are unbounded. Completed jobs remain in `jobMap`.
- Timing uses wall-clock time, so clock changes can affect delays.
- Task exceptions mark a job failed, including recurring jobs; they are not automatically retried. `Error` is outside that catch block.
- `TaskType.RUN_AFTER` exists in the enum but has no public scheduling API.

## Try it

Run [`job_scheduler.Main`](Main.java). It schedules a one-off job after five seconds and a recurring job after four seconds, waits six seconds, cancels the recurring job, and shuts down. The successful one-off status is `SUCCESS`; the demo's `COMPLETED` comment is stale.

For further discussion, consider bounded queues and retention first, then whether recurring jobs need fixed-rate behavior or persisted execution history.
