package parking_lot.entities;

import parking_lot.enums.VehicleType;
import parking_lot.state.PendingState;
import parking_lot.state.TicketState;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public class Ticket {

    private final String ticketId = UUID.randomUUID().toString();
    final ReentrantLock lock = new ReentrantLock();

    private String vehicleNumber;
    private VehicleType vehicleType;
    private List<Slot> slots;
    private Long entryAt;
    private TicketState currentState;

    public Ticket(String vehicleNumber, VehicleType vehicleType, List<Slot> slots, Long entryAt) {
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.slots = slots;
        this.entryAt = entryAt;
        this.currentState = new PendingState();
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public List<Slot> getSlots() {
        return slots;
    }

    public void setSlots(List<Slot> slots) {
        this.slots = slots;
    }

    public Long getEntryAt() {
        return entryAt;
    }

    public void setEntryAt(Long entryAt) {
        this.entryAt = entryAt;
    }

    public void setCurrentState(TicketState ticketState) {
        this.currentState = ticketState;
    }

    void expire() {
        this.currentState.onExpire(this);
    }

    void onCancel() {
        this.currentState.onCancel(this);
    }

    void paymentProcessed() {
        this.currentState.paymentProcessed(this);
    }
}
