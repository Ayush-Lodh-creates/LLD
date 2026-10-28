package parking_lot.state;

import parking_lot.entities.Ticket;

public interface TicketState {

    void onExpire(Ticket ticket);
    void onCancel(Ticket ticket);
    void paymentProcessed(Ticket ticket);
}
