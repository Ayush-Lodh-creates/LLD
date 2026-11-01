package notification_system.entities;

import notification_system.entities.delivery_strategy.DeliveryStrategy;
import notification_system.entities.delivery_strategy.HighStrategy;
import notification_system.entities.delivery_strategy.LowStrategy;
import notification_system.entities.delivery_strategy.MediumStrategy;
import notification_system.enums.Severity;

public class DeliveryFactory {

    public static DeliveryStrategy getStrategy(Severity severity) {
        switch (severity) {
            case LOW:
                return new LowStrategy();
            case MEDIUM:
                return new MediumStrategy();
            case HIGH:
                return new HighStrategy();
            default:
                throw new IllegalArgumentException("Unknown severity level");
        }
    }
}
