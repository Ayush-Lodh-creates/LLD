package job_scheduler;

import job_scheduler.enums.Priority;
import job_scheduler.services.JobScheduler;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        JobScheduler scheduler = new JobScheduler(2);

        String once = scheduler.scheduleOnce(() -> System.out.println("one-off ran"), 5000, Priority.MEDIUM);
        String recurring = scheduler.scheduleRecurring(() -> System.out.println("tick"), 4000, Priority.LOW, 10000);

        System.out.println(scheduler.getStatus(once));       // SCHEDULED
        Thread.sleep(6000);
        System.out.println(scheduler.getStatus(once));       // COMPLETED
        scheduler.cancel(recurring);
        System.out.println(scheduler.getStatus(recurring));  // CANCELLED
        scheduler.shutdown();
    }
}
