package food_booking.entities;

public interface OrderState {

    void processPayment(Order order);
    void cancel(Order order);
    String getStatus(Order order);
}
