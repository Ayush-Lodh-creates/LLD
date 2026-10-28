package parking_lot.entities.vehicle;

import parking_lot.enums.EngineType;
import parking_lot.enums.VehicleType;

public class Sedan extends Vehicle {

    public Sedan(String vehicleNumber, String vehicleName, Integer vehicleSize) {
        this(vehicleNumber, vehicleName, vehicleSize, EngineType.MOTOR);
    }

    public Sedan(String vehicleNumber, String vehicleName, Integer vehicleSize, EngineType engineType) {
        super(vehicleNumber, vehicleName, VehicleType.SEDAN, vehicleSize, engineType);
    }
}
