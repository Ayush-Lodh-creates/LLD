package parking_lot.strategy;

import parking_lot.entities.Ticket;
import parking_lot.enums.VehicleType;

public class HourlyPriceStrategy implements PriceCalculatorStrategy {

    @Override
    public int calculatePrice(Ticket ticket) {
        if(ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        }
        Long billingTime = System.currentTimeMillis();
        Long entry = ticket.getEntryAt();
        double hours = Math.ceil((double)(billingTime - entry) / (60*60*1000));
        int price = 0;
        if(ticket.getVehicleType() == VehicleType.TWO_WHEELER) {
            price = 25;
        } else if(ticket.getVehicleType() == VehicleType.LARGE_VEHICLE){
            price = 50 * ticket.getSlots().size();
        }
        price += (5*(int)hours);
        return price;
    }
}
