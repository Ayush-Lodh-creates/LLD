package parking_lot.state;

import parking_lot.entities.Slot;
import parking_lot.entities.Ticket;

import java.util.List;

public class PendingState implements TicketState {

    @Override
    public void onExpire(Ticket ticket) {
        System.out.println("Your booked slot has expired");
        List<Slot> slots = ticket.getSlots();
        for(Slot slot : slots) {
            try {
                slot.lock.lock();
                slot.setIsBooked(false);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                slot.lock.unlock();
            }
        }
        ticket.setCurrentState(new CancelState());
    }

    @Override
    public void onCancel(Ticket ticket) {
        System.out.println("Your booked slot has expired");
        List<Slot> slots = ticket.getSlots();
        for(Slot slot : slots) {
            try {
                slot.lock.lock();
                slot.setIsBooked(false);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                slot.lock.unlock();
            }
        }
        ticket.setCurrentState(new CancelState());
    }

    @Override
    public void paymentProcessed(Ticket ticket) {
        ticket.setCurrentState(new CompletedState());
    }
}
