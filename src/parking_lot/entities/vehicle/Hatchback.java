package parking_lot.entities.vehicle;

import parking_lot.enums.EngineType;
import parking_lot.enums.VehicleType;

public class Hatchback extends Vehicle {

    public Hatchback(String vehicleNumber, String vehicleName, Integer vehicleSize) {
        this(vehicleNumber, vehicleName, vehicleSize, EngineType.MOTOR);
    }

    public Hatchback(String vehicleNumber, String vehicleName, Integer vehicleSize, EngineType engineType) {
        super(vehicleNumber, vehicleName, VehicleType.HATCHBACK, vehicleSize, engineType);
    }
}
