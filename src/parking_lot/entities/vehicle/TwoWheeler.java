package parking_lot.entities.vehicle;

import parking_lot.enums.EngineType;
import parking_lot.enums.VehicleType;

public class TwoWheeler extends Vehicle {

    public TwoWheeler(String vehicleNumber, String vehicleName, Integer vehicleSize) {
        this(vehicleNumber, vehicleName, vehicleSize, EngineType.MOTOR);
    }

    public TwoWheeler(String vehicleNumber, String vehicleName, Integer vehicleSize, EngineType engineType) {
        super(vehicleNumber, vehicleName, VehicleType.TWO_WHEELER, vehicleSize, engineType);
    }
}
