package parking_lot.entities.discount_responsibility;

import parking_lot.entities.price_decorator.Price;
import parking_lot.enums.DiscountCoupon;

public class MembershipManager extends DiscountManager {

    @Override
    public void processDiscount(Price price, DiscountCoupon coupon) {
        if(coupon == DiscountCoupon.MEMBERSHIP) {
            int basePrice = price.getPrice();
            int discountedPrice = basePrice - (basePrice * 20 / 100);
            System.out.println("Discounted Price: " + discountedPrice);
        } else if (nextManager != null) {
            nextManager.processDiscount(price, coupon);
        }
    }
}
