package food_booking.entities;

public class CompletedState implements OrderState {

    @Override
    public void processPayment(Order order) {
        throw new IllegalArgumentException("Order is already completed, cannot process payment again.");
    }

    @Override
    public void cancel(Order order) {
        order.setState(new CancelledState());
    }

    @Override
    public String getStatus(Order order) {
        return "Completed";
    }
}
