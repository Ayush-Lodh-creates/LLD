package parking_lot.entities;

import parking_lot.enums.EventType;
import parking_lot.enums.SlotType;

import java.time.Instant;
import java.util.List;

public class Event {
    private final EventType type;
    private final Instant occurredAt;
    private final String ticketId;
    private final String vehicleNumber;
    private final SlotType slotType;
    private final List<String> slotCodes;

    public Event(EventType type, Ticket ticket, List<Slot> slots) {
        this.type = type;
        this.occurredAt = Instant.now();
        this.ticketId = ticket == null ? null : ticket.getTicketId();
        this.vehicleNumber = ticket == null ? null : ticket.getVehicleNumber();
        this.slotType = slots.get(0).getSlotType();
        // Copy details so later changes cannot alter an audit event.
        this.slotCodes = slots.stream().map(Slot::getSlotCode).toList();
    }

    public EventType getType() { return type; }
    public Instant getOccurredAt() { return occurredAt; }
    public String getTicketId() { return ticketId; }
    public String getVehicleNumber() { return vehicleNumber; }
    public SlotType getSlotType() { return slotType; }
    public List<String> getSlotCodes() { return slotCodes; }
}
