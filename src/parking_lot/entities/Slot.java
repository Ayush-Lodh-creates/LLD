package parking_lot.entities;

import parking_lot.enums.SlotType;

import java.util.concurrent.locks.ReentrantLock;

public class Slot {

    private String slotCode;
    private Boolean isBooked;
    private SlotType slotType;
    public ReentrantLock lock;

    public Slot(String slotCode, SlotType slotType) {
        this.slotCode = slotCode;
        this.isBooked = false;
        this.slotType = slotType;
        this.lock = new ReentrantLock();
    }

    public String getSlotCode() {
        return slotCode;
    }

    public void setSlotCode(String slotCode) {
        this.slotCode = slotCode;
    }

    public Boolean getIsBooked() {
        return isBooked;
    }

    public void setIsBooked(Boolean isBooked) {
        this.isBooked = isBooked;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public void setSlotType(SlotType slotType) {
        this.slotType = slotType;
    }
}
