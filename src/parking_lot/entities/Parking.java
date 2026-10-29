package parking_lot.entities;

import parking_lot.entities.vehicle.Vehicle;
import parking_lot.enums.BookingType;
import parking_lot.enums.EventType;
import parking_lot.enums.SlotType;
import parking_lot.enums.VehicleType;
import parking_lot.factory.SlotFactory;
import parking_lot.observer.EventListener;
import parking_lot.observer.AuditListener;
import parking_lot.observer.BoardDisplayListener;
import parking_lot.state.ExitedState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Parking {

    private final BoardDisplayListener boardDisplayListener;

    private int twoWheelerSlotCount;
    private int fourWheelerSlotCount;

    private List<Slot> twoWheelerSlots;
    private List<Slot> fourWheelerSlots;
    private List<EventListener> eventListeners;
    private Map<String, Ticket> pendingTicketsMap;
    // Tracks every active allocation, including pending reservations and paid bookings.
    private final Map<String, Ticket> bookedTickets = new ConcurrentHashMap<>();
    // Readers may allocate concurrently; writers change the inventory structure.
    private final ReentrantReadWriteLock inventoryLock = new ReentrantReadWriteLock();

    public Parking(int twoWheelerSlots, int fourWheelerSlots) {
        this.twoWheelerSlots = new ArrayList<>();
        this.eventListeners = new CopyOnWriteArrayList<>();
        this.fourWheelerSlots = new ArrayList<>();
        boardDisplayListener = new BoardDisplayListener();
        eventListeners.add(new AuditListener());
        eventListeners.add(boardDisplayListener);
        initialize(twoWheelerSlots, fourWheelerSlots);
        pendingTicketsMap = new ConcurrentHashMap<>();
    }

    private void initialize(int twoWheelerSlots, int fourWheelerSlots) {
        for (int i = 0; i < twoWheelerSlots; i++) {
            addTwoWheelerSlot();
        }
        for (int i = 0; i < fourWheelerSlots; i++) {
            addFourWheelerSlot();
        }
    }

    public synchronized void addEventListeners(EventListener eventListener) {
        this.eventListeners.add(Objects.requireNonNull(eventListener));
    }

    public synchronized void removeEventListeners(EventListener eventListener) {
        this.eventListeners.remove(eventListener);
    }

    public BoardDisplayListener getBoardDisplayListener() {
        return boardDisplayListener;
    }

    private void publishEvent(Event event) {
        for (EventListener listener : eventListeners) {
            try {
                listener.onEvent(event);
            } catch (RuntimeException e) {
                // A broken listener must not undo a successful parking operation.
                System.err.println("Listener " + listener.getClass().getSimpleName()
                        + " failed: " + e.getMessage());
            }
        }
    }

    private void publishTicketEvent(EventType type, Ticket ticket) {
        publishEvent(new Event(type, ticket, ticket.getSlots()));
    }

    public void addTwoWheelerSlot() {
        inventoryLock.writeLock().lock();
        try {
            twoWheelerSlotCount++;
            Slot slot = SlotFactory.createSlot("TW" + twoWheelerSlotCount, SlotType.TWO_WHEELER);
            twoWheelerSlots.add(slot);
            publishEvent(new Event(EventType.SLOT_ADDED, null, List.of(slot)));
        } finally {
            inventoryLock.writeLock().unlock();
        }
    }

    public void addFourWheelerSlot() {
        inventoryLock.writeLock().lock();
        try {
            fourWheelerSlotCount++;
            Slot slot = SlotFactory.createSlot("FW" + fourWheelerSlotCount, SlotType.FOUR_WHEELER);
            fourWheelerSlots.add(slot);
            publishEvent(new Event(EventType.SLOT_ADDED, null, List.of(slot)));
        } finally {
            inventoryLock.writeLock().unlock();
        }
    }

    public void removeTwoWheelerSlot() {
        inventoryLock.writeLock().lock();
        try {
            for (int i = twoWheelerSlots.size() - 1; i >= 0; i--) {
                Slot slot = twoWheelerSlots.get(i);
                if (!slot.lock.tryLock()) {
                    continue;
                }
                try {
                    if (!slot.getIsBooked()) {
                        twoWheelerSlots.remove(i);
                        publishEvent(new Event(EventType.SLOT_REMOVED, null, List.of(slot)));
                        return;
                    }
                } finally {
                    slot.lock.unlock();
                }
            }
            System.out.println("No unbooked two wheeler slot available to remove");
        } finally {
            inventoryLock.writeLock().unlock();
        }
    }

    public void removeFourWheelerSlot() {
        inventoryLock.writeLock().lock();
        try {
            for (int i = fourWheelerSlots.size() - 1; i >= 0; i--) {
                Slot slot = fourWheelerSlots.get(i);
                if (!slot.lock.tryLock()) {
                    continue;
                }
                try {
                    if (!slot.getIsBooked()) {
                        fourWheelerSlots.remove(i);
                        publishEvent(new Event(EventType.SLOT_REMOVED, null, List.of(slot)));
                        return;
                    }
                } finally {
                    slot.lock.unlock();
                }
            }
            System.out.println("No unbooked four wheeler slot available to remove");
        } finally {
            inventoryLock.writeLock().unlock();
        }
    }

    private Slot getNextAvailableTwoWheelerSlot() {
        inventoryLock.readLock().lock();
        try {
            for (Slot slot : twoWheelerSlots) {
                if (!slot.lock.tryLock()) {
                    continue;
                }
                try {
                    if (!slot.getIsBooked()) {
                        slot.setIsBooked(true);
                        return slot;
                    }
                } finally {
                    slot.lock.unlock();
                }
            }
            return null;
        } finally {
            inventoryLock.readLock().unlock();
        }
    }

    private List<Slot> getNextAvailableFourWheelerSlot(Vehicle vehicle) {
        inventoryLock.readLock().lock();
        try {
            List<Slot> slots = new ArrayList<>();
            if(vehicle.getVehicleType() != VehicleType.LARGE_VEHICLE) {
                for (Slot slot : fourWheelerSlots) {
                    if (!slot.lock.tryLock()) {
                        continue;
                    }
                    try {
                        if (!slot.getIsBooked()) {
                            slot.setIsBooked(true);
                            slots.add(slot);
                            return slots;
                        }
                    } finally {
                        slot.lock.unlock();
                    }
                }
            } else {
                Integer size = vehicle.getVehicleSize();
                if (size == null || size < 2 || size > fourWheelerSlots.size()) {
                    return null;
                }
                for (int i = 0; i <= fourWheelerSlots.size() - size; i++) {
                    slots = new ArrayList<>();
                    int firstSlotNumber = Integer.parseInt(fourWheelerSlots.get(i).getSlotCode().substring(2));
                    try {
                        for (int j = 0; j < size; j++) {
                            Slot slot = fourWheelerSlots.get(i + j);
                            // Removed slots must not make separated spaces look consecutive.
                            if (!slot.getSlotCode().equals("FW" + (firstSlotNumber + j))) {
                                break;
                            }
                            if (!slot.lock.tryLock()) {
                                break;
                            }
                            slots.add(slot);
                            if (slot.getIsBooked()) {
                                break;
                            }
                            if (j == size - 1) {
                                for (Slot selectedSlot : slots) {
                                    selectedSlot.setIsBooked(true);
                                }
                                return slots;
                            }
                        }
                    } finally {
                        for (Slot selectedSlot : slots) {
                            selectedSlot.lock.unlock();
                        }
                    }
                }
            }
            return null;
        } finally {
            inventoryLock.readLock().unlock();
        }
    }

    public Ticket bookSlots(Vehicle vehicle, BookingType bookingType) {
        Objects.requireNonNull(vehicle, "Vehicle is required");
        Objects.requireNonNull(bookingType, "Booking type is required");
        String vehicleNumber = Objects.requireNonNull(vehicle.getVehicleNumber(), "Vehicle number is required");
        Ticket ticket = new Ticket(vehicleNumber, vehicle.getVehicleType(), List.of(), System.currentTimeMillis());

        // Atomic registration prevents duplicates; this new ticket is not exposed to callers yet.
        boolean registered = false;
        boolean completed = false;
        boolean allocationAnnounced = false;
        try {
            if (bookedTickets.putIfAbsent(vehicleNumber, ticket) != null) {
                System.out.println("Vehicle already has a reservation or booking: " + vehicleNumber);
                return null;
            }
            registered = true;

            List<Slot> slots;
            if (vehicle.getVehicleType() == VehicleType.TWO_WHEELER) {
                Slot slot = getNextAvailableTwoWheelerSlot();
                slots = slot == null ? List.of() : List.of(slot);
            } else {
                slots = getNextAvailableFourWheelerSlot(vehicle);
            }
            if (slots == null || slots.isEmpty()) {
                System.out.println("No suitable slots available right now");
                return null;
            }

            ticket.setSlots(List.copyOf(slots));
            ticket.setEntryAt(System.currentTimeMillis());
            publishTicketEvent(bookingType == BookingType.RESERVATION
                    ? EventType.SLOTS_RESERVED : EventType.SLOTS_BOOKED, ticket);
            allocationAnnounced = true;
            if (bookingType == BookingType.ON_DEMAND) {
                // Finish the simulated payment before exposing this ticket to lifecycle operations.
                ticket.paymentProcessed();
                publishTicketEvent(EventType.PAYMENT_PROCESSED, ticket);
            } else {
                // Expiry can see the ticket from here onward; initialization is finished.
                pendingTicketsMap.put(vehicleNumber, ticket);
            }
            completed = true;
            return ticket;
        } finally {
            if (registered && !completed) {
                // Failed allocation must not prevent a later booking for this vehicle.
                ticket.onCancel();
                if (allocationAnnounced) {
                    publishTicketEvent(EventType.TICKET_CANCELLED, ticket);
                }
                pendingTicketsMap.remove(vehicleNumber, ticket);
                bookedTickets.remove(vehicleNumber, ticket);
            }
        }
    }

    public void freeSlots(Ticket ticket) {
        Objects.requireNonNull(ticket, "Ticket is required");
        ticket.lock.lock();
        try {
            // Only a paid, active ticket may exit. Old tickets cannot free a newer allocation.
            if (bookedTickets.get(ticket.getVehicleNumber()) != ticket
                    || pendingTicketsMap.get(ticket.getVehicleNumber()) == ticket) {
                return;
            }
            for (Slot slot : ticket.getSlots()) {
                slot.lock.lock();
                try {
                    slot.setIsBooked(false);
                } finally {
                    slot.lock.unlock();
                }
            }
            bookedTickets.remove(ticket.getVehicleNumber(), ticket);
            ticket.setCurrentState(new ExitedState());
            publishTicketEvent(EventType.SLOTS_FREED, ticket);
        } finally {
            ticket.lock.unlock();
        }
    }

    public void processPayment(Ticket ticket) {
        ticket.lock.lock();
        try {
            if (pendingTicketsMap.get(ticket.getVehicleNumber()) != ticket) {
                return;
            }
            if (System.currentTimeMillis() - ticket.getEntryAt() >= 15 * 60 * 1000L) {
                ticket.expire();
                pendingTicketsMap.remove(ticket.getVehicleNumber(), ticket);
                bookedTickets.remove(ticket.getVehicleNumber(), ticket);
                publishTicketEvent(EventType.TICKET_EXPIRED, ticket);
                return;
            }
            // Simulate payment processing.
            System.out.println("Payment processed for Slots : " + ticket.getSlots());
            ticket.paymentProcessed();
            pendingTicketsMap.remove(ticket.getVehicleNumber(), ticket);
            publishTicketEvent(EventType.PAYMENT_PROCESSED, ticket);

        } finally {
            ticket.lock.unlock();
        }
    }

    public void cancelTicket(Ticket ticket) {
        ticket.lock.lock();
        try {
            if (pendingTicketsMap.get(ticket.getVehicleNumber()) != ticket) {
                return;
            }
            ticket.onCancel();
            publishTicketEvent(EventType.TICKET_CANCELLED, ticket);
            pendingTicketsMap.remove(ticket.getVehicleNumber(), ticket);
            bookedTickets.remove(ticket.getVehicleNumber(), ticket);
        } finally {
            ticket.lock.unlock();
        }
    }

    public void expireTickets() {
        for (Ticket ticket : pendingTicketsMap.values()) {
            ticket.lock.lock();
            try {
                if (pendingTicketsMap.get(ticket.getVehicleNumber()) != ticket) {
                    continue;
                }
                if (System.currentTimeMillis() - ticket.getEntryAt() >= 15 * 60 * 1000L) {
                    ticket.expire();
                    publishTicketEvent(EventType.TICKET_EXPIRED, ticket);
                    pendingTicketsMap.remove(ticket.getVehicleNumber(), ticket);
                    bookedTickets.remove(ticket.getVehicleNumber(), ticket);
                }
            } finally {
                ticket.lock.unlock();
            }
        }
    }
}
