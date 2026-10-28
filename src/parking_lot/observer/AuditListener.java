package parking_lot.observer;

import parking_lot.entities.Event;

public class AuditListener implements EventListener {
    @Override
    public void onEvent(Event event) {
        System.out.printf("[AUDIT] %s | %s | ticket=%s | vehicle=%s | type=%s | slots=%s%n",
                event.getOccurredAt(), event.getType(), event.getTicketId(),
                event.getVehicleNumber(), event.getSlotType(), event.getSlotCodes());
    }
}
