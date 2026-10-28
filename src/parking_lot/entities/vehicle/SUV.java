package parking_lot.entities.vehicle;

import parking_lot.enums.EngineType;
import parking_lot.enums.VehicleType;

public class SUV extends Vehicle {

    public SUV(String vehicleNumber, String vehicleName, Integer vehicleSize) {
        this(vehicleNumber, vehicleName, vehicleSize, EngineType.MOTOR);
    }

    public SUV(String vehicleNumber, String vehicleName, Integer vehicleSize, EngineType engineType) {
        super(vehicleNumber, vehicleName, VehicleType.SUV, vehicleSize, engineType);
    }
}
