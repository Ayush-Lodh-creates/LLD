package parking_lot.entities.price_decorator;

import parking_lot.entities.Ticket;
import parking_lot.strategy.PriceCalculatorStrategy;

public class BasePrice implements Price {

    private PriceCalculatorStrategy priceCalculatorStrategy;
    private Ticket ticket;

    public BasePrice(PriceCalculatorStrategy priceCalculatorStrategy, Ticket ticket) {
        this.priceCalculatorStrategy = priceCalculatorStrategy;
        this.ticket = ticket;
    }

    @Override
    public int getPrice() {
        return priceCalculatorStrategy.calculatePrice(ticket);
    }

    @Override
    public String getDescription() {
        return "Base Price for vehicles";
    }
}
