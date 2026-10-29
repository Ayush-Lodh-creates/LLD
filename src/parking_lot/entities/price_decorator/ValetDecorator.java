package parking_lot.entities.price_decorator;

public class ValetDecorator extends PriceDecorator {

    public ValetDecorator(Price price) {
        super(price);
    }

    @Override
    public int getPrice() {
        return price.getPrice() + 10; // Adding $10 for valet service
    }

    @Override
    public String getDescription() {
        return price.getDescription() + " + Valet service";
    }
}
