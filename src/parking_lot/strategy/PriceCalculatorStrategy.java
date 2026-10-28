package parking_lot.strategy;

import parking_lot.entities.Ticket;

public interface PriceCalculatorStrategy {

    public int calculatePrice(Ticket ticket);
}
