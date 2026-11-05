package food_booking.entities;

import food_booking.OrderFactory;
import food_booking.RestaurantManager;
import food_booking.enums.OrderType;

import java.util.HashMap;
import java.util.Map;

public class Cart {

    private Restaurant restaurant;
    private Map<Integer, Integer> itemsToQuantity;
    private double total;
    private Order pendingOrder;

    public Cart() {
        itemsToQuantity = new HashMap<>();
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Map<Integer, Integer> getItems() {
        return itemsToQuantity;
    }

    public void setItems(Map<Integer, Integer> items) {
        this.itemsToQuantity = items;
    }

    public void addItemsToCart(Restaurant restaurant, Map<Integer, Integer> items, double total) {
        this.restaurant = restaurant;
        this.itemsToQuantity.putAll(items);
        this.total = total;
    }

    public Order confirmBooking(RestaurantManager restaurantManager, OrderType orderType, User user, PaymentStrategy paymentStrategy) {
        restaurantManager.confirmCart(this);
        Order order = OrderFactory.createOrder(restaurantManager.orderIdCounter.getAndIncrement(), restaurant, itemsToQuantity, total, user, paymentStrategy, orderType);
        pendingOrder = order;
        order.setCheckoutCart(this);
        return order;
    }

    void finishCheckout(Order order) {
        if (pendingOrder == order && restaurant == order.getRestaurant() && itemsToQuantity.equals(order.getItemsToQuantity())) {
            clearCart();
        }
    }

    public void clearCart() {
        pendingOrder = null;
        this.restaurant = null;
        this.itemsToQuantity.clear();
        this.total = 0.0;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double value) {
        this.total = value;
    }
}
