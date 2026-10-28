package parking_lot.entities;

import parking_lot.entities.vehicle.Vehicle;
import parking_lot.enums.BookingType;
import parking_lot.enums.SlotType;
import parking_lot.enums.VehicleType;
import parking_lot.factory.SlotFactory;
import parking_lot.observer.EventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Parking {

    private int twoWheelerSlotCount;
    private int fourWheelerSlotCount;

    private List<Slot> twoWheelerSlots;
    private List<Slot> fourWheelerSlots;
    private List<EventListener> eventListeners;
    private Map<String, Ticket> pendingTicketsMap;
    private List<Ticket> bookedTickets;
    private final Map<String, Ticket> ticketsById = new ConcurrentHashMap<>();

    public Parking(int twoWheelerSlots, int fourWheelerSlots) {
        this.twoWheelerSlots = new ArrayList<>();
        this.eventListeners = new CopyOnWriteArrayList<>();
        this.fourWheelerSlots = new ArrayList<>();
        initialize(twoWheelerSlots, fourWheelerSlots);
        pendingTicketsMap = new ConcurrentHashMap<>();
        bookedTickets = new CopyOnWriteArrayList<>();
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
        this.eventListeners.add(eventListener);
    }

    private synchronized void removeEventListeners(EventListener eventListener) {
        this.eventListeners.remove(eventListener);
    }

    public synchronized void addTwoWheelerSlot() {
        twoWheelerSlotCount++;
        twoWheelerSlots.add(SlotFactory.createSlot("TW" + twoWheelerSlotCount, SlotType.TWO_WHEELER));
    }

    public synchronized void addFourWheelerSlot() {
        fourWheelerSlotCount++;
        fourWheelerSlots.add(SlotFactory.createSlot("FW" + fourWheelerSlotCount, SlotType.FOUR_WHEELER));
    }

    public synchronized void removeTwoWheelerSlot() {
        for (int i = twoWheelerSlots.size() - 1; i >= 0; i--) {
            Slot slot = twoWheelerSlots.get(i);
            if (!slot.lock.tryLock()) {
                continue;
            }
            try {
                if (!slot.getIsBooked()) {
                    twoWheelerSlots.remove(i);
                    return;
                }
            } finally {
                slot.lock.unlock();
            }
        }
        System.out.println("No unbooked two wheeler slot available to remove");
    }

    public synchronized void removeFourWheelerSlot() {
        for (int i = fourWheelerSlots.size() - 1; i >= 0; i--) {
            Slot slot = fourWheelerSlots.get(i);
            if (!slot.lock.tryLock()) {
                continue;
            }
            try {
                if (!slot.getIsBooked()) {
                    fourWheelerSlots.remove(i);
                    return;
                }
            } finally {
                slot.lock.unlock();
            }
        }
        System.out.println("No unbooked four wheeler slot available to remove");
    }

    private synchronized Slot getNextAvailableTwoWheelerSlot() {
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
    }

    private synchronized List<Slot> getNextAvailableFourWheelerSlot(Vehicle vehicle) {
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
    }

    public synchronized Ticket bookSlots(Vehicle vehicle, BookingType bookingType) {
        // Keep the duplicate check and allocation together under the lot lock.
        String vehicleNumber = vehicle.getVehicleNumber();
        if (pendingTicketsMap.containsKey(vehicleNumber)
                || bookedTickets.stream().anyMatch(ticket -> ticket.getVehicleNumber().equals(vehicleNumber))) {
            System.out.println("Vehicle already has a reservation or booking: " + vehicleNumber);
            return null;
        }
        if(bookingType == BookingType.RESERVATION) {
            if(vehicle.getVehicleType() == VehicleType.TWO_WHEELER) {
                Slot slot = getNextAvailableTwoWheelerSlot();
                if (slot != null) {
                    Ticket ticket = new Ticket(vehicle.getVehicleNumber(), vehicle.getVehicleType(), List.of(slot), System.currentTimeMillis());
                    ticketsById.put(ticket.getTicketId(), ticket);
                    pendingTicketsMap.put(vehicle.getVehicleNumber(), ticket);
                    System.out.println("Slot reserved successfully for vehicle: " + vehicle.getVehicleNumber());
                    return ticket;
                } else {
                    System.out.println("No two wheeler slot available");
                    return null;
                }
            } else {
                List<Slot> slots = getNextAvailableFourWheelerSlot(vehicle);
                if (slots != null && !slots.isEmpty()) {
                    Ticket ticket = new Ticket(vehicle.getVehicleNumber(), vehicle.getVehicleType(), slots, System.currentTimeMillis());
                    ticketsById.put(ticket.getTicketId(), ticket);
                    pendingTicketsMap.put(vehicle.getVehicleNumber(), ticket);
                    System.out.println("Slot reserved successfully for vehicle: " + vehicle.getVehicleNumber());
                    return ticket;
                } else {
                    System.out.println("No four wheeler slot available");
                    return null;
                }
            }
        } else {
            if(vehicle.getVehicleType() == VehicleType.TWO_WHEELER) {
                Slot slot = getNextAvailableTwoWheelerSlot();
                if (slot != null) {
                    Ticket ticket = new Ticket(vehicle.getVehicleNumber(), vehicle.getVehicleType(), List.of(slot), System.currentTimeMillis());
                    ticketsById.put(ticket.getTicketId(), ticket);
                    pendingTicketsMap.put(vehicle.getVehicleNumber(), ticket);
                    System.out.println("Slot booked successfully for vehicle: " + vehicle.getVehicleNumber());
                    processPayment(ticket);
                    return ticket;
                } else {
                    System.out.println("No two wheeler slot available");
                    return null;
                }
            } else {
                List<Slot> slots = getNextAvailableFourWheelerSlot(vehicle);
                if (slots != null && !slots.isEmpty()) {
                    Ticket ticket = new Ticket(vehicle.getVehicleNumber(), vehicle.getVehicleType(), slots, System.currentTimeMillis());
                    ticketsById.put(ticket.getTicketId(), ticket);
                    pendingTicketsMap.put(vehicle.getVehicleNumber(), ticket);
                    System.out.println("Slot booked successfully for vehicle: " + vehicle.getVehicleNumber());
                    processPayment(ticket);
                    return ticket;
                } else {
                    System.out.println("No four wheeler slot available");
                    return null;
                }
            }
        }
    }

    

    public Ticket getTicket(String ticketId) {
        return ticketsById.get(ticketId);
    }

    public void processPayment(Ticket ticket) {
        ticket.lock.lock();
        try {
            if (ticketsById.get(ticket.getTicketId()) != ticket
                    || pendingTicketsMap.get(ticket.getVehicleNumber()) != ticket) {
                return;
            }
            // Simulate payment processing.
            System.out.println("Payment processed for Slots : " + ticket.getSlots());
            ticket.paymentProcessed();
            pendingTicketsMap.remove(ticket.getVehicleNumber(), ticket);
            bookedTickets.add(ticket);
            // Keep paid tickets registered until the vehicle exits.
        } finally {
            ticket.lock.unlock();
        }
    }

    public void cancelTicket(Ticket ticket) {
        ticket.lock.lock();
        try {
            if (ticketsById.get(ticket.getTicketId()) != ticket
                    || pendingTicketsMap.get(ticket.getVehicleNumber()) != ticket) {
                return;
            }
            ticket.onCancel();
            pendingTicketsMap.remove(ticket.getVehicleNumber(), ticket);
            ticketsById.remove(ticket.getTicketId(), ticket);
        } finally {
            ticket.lock.unlock();
        }
    }

    public void expireTickets() {
        for (Ticket ticket : pendingTicketsMap.values()) {
            ticket.lock.lock();
            try {
                if (ticketsById.get(ticket.getTicketId()) != ticket
                        || pendingTicketsMap.get(ticket.getVehicleNumber()) != ticket) {
                    continue;
                }
                if (System.currentTimeMillis() - ticket.getEntryAt() >= 15 * 60 * 1000L) {
                    ticket.expire();
                    pendingTicketsMap.remove(ticket.getVehicleNumber(), ticket);
                    ticketsById.remove(ticket.getTicketId(), ticket);
                }
            } finally {
                ticket.lock.unlock();
            }
        }
    }
}
