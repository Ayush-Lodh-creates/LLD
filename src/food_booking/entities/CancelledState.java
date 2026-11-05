package food_booking.entities;

public class CancelledState implements OrderState {

    @Override
    public void processPayment(Order order) {
        throw new IllegalArgumentException("Order is already cancelled, cannot process payment.");
    }

    @Override
    public void cancel(Order order) {
        throw new IllegalArgumentException("Order is already cancelled.");
    }

    @Override
    public String getStatus(Order order) {
        return "Cancelled";
    }
}
