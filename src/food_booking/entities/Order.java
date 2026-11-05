package food_booking.entities;

import food_booking.RestaurantManager;
import food_booking.enums.OrderType;

import java.util.Map;

public abstract class Order {

    private int id;
    private Restaurant restaurant;
    private final Map<Integer, Integer> itemsToQuantity;
    private Cart checkoutCart;
    private double total;
    private User user;
    private PaymentStrategy paymentStrategy;
    private OrderState state;
    protected OrderType orderType;

    public Order(int id, Restaurant restaurant, Map<Integer, Integer> itemsToQuantity, double total, User user, PaymentStrategy paymentStrategy) {
        this.id = id;
        this.restaurant = restaurant;
        this.itemsToQuantity = Map.copyOf(itemsToQuantity);
        this.total = total;
        this.user = user;
        this.paymentStrategy = paymentStrategy;
        this.state = new PaymentProcessingState();
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public void processPayment() {
        if(state.getStatus(this).equalsIgnoreCase("Payment Processing")) {
            paymentStrategy.processPayment();
            state.processPayment(this);
            if (checkoutCart != null) {
                checkoutCart.finishCheckout(this);
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Map<Integer, Integer> getItemsToQuantity() {
        return itemsToQuantity;
    }

    void setCheckoutCart(Cart cart) {
        this.checkoutCart = cart;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PaymentStrategy getPaymentStrategy() {
        return paymentStrategy;
    }

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void cancel(RestaurantManager restaurantManager) {
        if(state.getStatus(this).equalsIgnoreCase("Payment Processing")) {
            state.cancel(this);
            restaurantManager.cancelOrder(this);
            System.out.println("Order cancelled successfully with id : " + id);
        } else if (state.getStatus(this).equalsIgnoreCase("Confirmed")) {
            state.cancel(this);
            restaurantManager.cancelOrder(this);
            System.out.println("Order cancelled successfully with id : " + id + " Payment of " + total + " will be refunded.");
        }
    }
}
