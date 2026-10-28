package parking_lot.entities.vehicle;

import parking_lot.enums.EngineType;
import parking_lot.enums.VehicleType;

public class LargeVehicle extends Vehicle {

    public LargeVehicle(String vehicleNumber, String vehicleName, Integer vehicleSize) {
        this(vehicleNumber, vehicleName, vehicleSize, EngineType.MOTOR);
    }

    public LargeVehicle(String vehicleNumber, String vehicleName, Integer vehicleSize, EngineType engineType) {
        super(vehicleNumber, vehicleName, VehicleType.LARGE_VEHICLE, vehicleSize, engineType);
    }
}
