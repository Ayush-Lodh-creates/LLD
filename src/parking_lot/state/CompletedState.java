package parking_lot.state;

import parking_lot.entities.Ticket;

public class CompletedState implements TicketState {

    @Override
    public void onExpire(Ticket ticket) {
        System.out.println("Ticket is already completed. Cannot expire.");
    }

    @Override
    public void onCancel(Ticket ticket) {
        System.out.println("Ticket is already completed. Cannot cancel.");
    }

    @Override
    public void paymentProcessed(Ticket ticket) {
        System.out.println("Payment has already been processed for this ticket.");
    }
}
