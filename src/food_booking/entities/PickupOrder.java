package food_booking.entities;

import food_booking.enums.OrderType;

import java.util.Map;

public class PickupOrder extends Order {

    public PickupOrder(int id, Restaurant restaurant, Map<Integer, Integer> itemsToQuantity, double total, User user, PaymentStrategy paymentStrategy) {
        super(id, restaurant, itemsToQuantity, total, user, paymentStrategy);
        super.orderType = OrderType.PICKUP;
    }
}
