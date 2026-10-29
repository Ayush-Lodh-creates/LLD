package parking_lot.entities.discount_responsibility;

import parking_lot.entities.price_decorator.Price;
import parking_lot.enums.DiscountCoupon;

public class FlatManager extends DiscountManager {

    @Override
    public void processDiscount(Price price, DiscountCoupon coupon) {
        if (coupon == DiscountCoupon.FLAT) {
            int basePrice = price.getPrice();
            int discountedPrice = basePrice - 15; // Apply a flat discount of 15
            System.out.println("Flat discount applied. New price: " + discountedPrice);
        } else {
            throw new IllegalArgumentException("Invalid discount coupon for FlatManager");
        }
    }
}
