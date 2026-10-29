package parking_lot.entities.price_decorator;

public class EVDecorator extends PriceDecorator {

    public EVDecorator(Price price) {
        super(price);
    }
    @Override
    public int getPrice() {
        return price.getPrice() + 5; // Adding $20 for EV vehicles
    }

    @Override
    public String getDescription() {
        return price.getDescription() + " + Additional charge for EV vehicles";
    }
}
