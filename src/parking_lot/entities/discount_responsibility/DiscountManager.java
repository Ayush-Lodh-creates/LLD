package parking_lot.entities.discount_responsibility;

import parking_lot.entities.price_decorator.Price;
import parking_lot.enums.DiscountCoupon;

public abstract class DiscountManager {

    protected DiscountManager nextManager;

    public void setNextManager(DiscountManager discountManager) {
        this.nextManager = discountManager;
    }

    public abstract void processDiscount(Price price, DiscountCoupon coupon);
}
