package job_scheduler.enums;

public enum Priority {

    LOW(1),
    MEDIUM(2),
    HIGH(3);

    public int value;

    Priority(int value) {
        this.value = value;
    }
}
