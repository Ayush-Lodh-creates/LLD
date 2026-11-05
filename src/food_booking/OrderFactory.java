package food_booking;

import food_booking.entities.*;
import food_booking.enums.OrderType;

import java.util.Map;

public class OrderFactory {

    public static Order createOrder(int id, Restaurant restaurant, Map<Integer, Integer> items, double total, User user, PaymentStrategy paymentStrategy, OrderType orderType) {
        switch (orderType) {
            case PICKUP:
                return new PickupOrder(id, restaurant, items, total, user, paymentStrategy);
            case DELIVERY:
                return new DeliveryOrder(id, restaurant, items, total, user, paymentStrategy);
            default:
                throw new IllegalArgumentException("Invalid order type: " + orderType);
        }
    }
}
