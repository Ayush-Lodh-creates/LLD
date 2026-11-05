package food_booking.entities;

public class PaymentProcessingState implements OrderState {

    @Override
    public void processPayment(Order order) {
        order.setState(new CompletedState());
    }

    @Override
    public void cancel(Order order) {
        order.setState(new CancelledState());
    }

    @Override
    public String getStatus(Order order) {
        return "Payment Processing";
    }
}
