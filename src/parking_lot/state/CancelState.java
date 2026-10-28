package parking_lot.state;

import parking_lot.entities.Ticket;

public class CancelState implements TicketState {

    @Override
    public void onExpire(Ticket ticket) {
        System.out.println("Ticket is already cancelled. Cannot expire.");
    }

    @Override
    public void onCancel(Ticket ticket) {
        System.out.println("Ticket is already cancelled.");
    }

    @Override
    public void paymentProcessed(Ticket ticket) {
        System.out.println("Payment cannot be processed for a cancelled ticket.");
    }
}
