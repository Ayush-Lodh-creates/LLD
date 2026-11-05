package food_booking.entities;

import food_booking.enums.OrderType;

import java.util.Map;

public class DeliveryOrder extends Order {

    public DeliveryOrder(int id, Restaurant restaurant, Map<Integer, Integer> itemsToQuantity, double total, User user, PaymentStrategy paymentStrategy) {
        super(id, restaurant, itemsToQuantity, total, user, paymentStrategy);
        super.orderType = OrderType.DELIVERY;
    }
}
