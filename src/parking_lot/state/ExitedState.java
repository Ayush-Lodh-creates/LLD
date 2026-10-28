package parking_lot.state;

import parking_lot.entities.Ticket;

public class ExitedState implements TicketState {

    @Override
    public void onExpire(Ticket ticket) {
        System.out.println("Vehicle has already exited. Cannot expire ticket.");
    }

    @Override
    public void onCancel(Ticket ticket) {
        System.out.println("Vehicle has already exited. Cannot cancel ticket.");
    }

    @Override
    public void paymentProcessed(Ticket ticket) {
        System.out.println("Vehicle has already exited. Payment is already complete.");
    }
}
