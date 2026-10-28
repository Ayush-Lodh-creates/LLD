package parking_lot.observer;

import parking_lot.entities.Event;
import parking_lot.enums.SlotType;

public class BoardDisplayListener implements EventListener {
    private int availableTwoWheelerSlots;
    private int availableFourWheelerSlots;

    @Override
    public synchronized void onEvent(Event event) {
        int count = event.getSlotCodes().size();
        int change = switch (event.getType()) {
            case SLOT_ADDED, TICKET_CANCELLED, TICKET_EXPIRED, SLOTS_FREED -> count;
            case SLOT_REMOVED, SLOTS_RESERVED, SLOTS_BOOKED -> -count;
            case PAYMENT_PROCESSED -> 0;
        };
        if (change == 0) {
            return;
        }
        if (event.getSlotType() == SlotType.TWO_WHEELER) {
            availableTwoWheelerSlots += change;
        } else {
            availableFourWheelerSlots += change;
        }
        display();
    }

    public synchronized void display() {
        System.out.printf("[BOARD] Free spots: 2W=%d, 4W=%d%n",
                availableTwoWheelerSlots, availableFourWheelerSlots);
    }

    public synchronized int getAvailableTwoWheelerSlots() {
        return availableTwoWheelerSlots;
    }

    public synchronized int getAvailableFourWheelerSlots() {
        return availableFourWheelerSlots;
    }
}
