package parking_lot.entities.price_decorator;

public abstract class PriceDecorator implements Price {

    protected Price price;

    public PriceDecorator(Price price) {
        this.price = price;
    }
}
