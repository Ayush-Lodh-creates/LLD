package parking_lot.factory;

import parking_lot.entities.vehicle.Vehicle;
import parking_lot.entities.vehicle.Sedan;
import parking_lot.entities.vehicle.ElectricSedan;
import parking_lot.entities.vehicle.SUV;
import parking_lot.entities.vehicle.Hatchback;
import parking_lot.entities.vehicle.TwoWheeler;
import parking_lot.entities.vehicle.LargeVehicle;
import parking_lot.enums.VehicleType;
import parking_lot.enums.EngineType;

public class VehicleFactory {

    public static Vehicle createVehicle(String vehicleNumber, String vehicleName, VehicleType vehicleType, Integer vehicleSize, EngineType engineType) {
        if (vehicleType == null || engineType == null) {
            throw new IllegalArgumentException("Vehicle type and engine type are required");
        }
        switch (vehicleType) {
            case TWO_WHEELER -> {
                return new TwoWheeler(vehicleNumber, vehicleName, vehicleSize, engineType);
            }
            case SEDAN -> {
                if (engineType == EngineType.ELECTRIC) {
                    return new ElectricSedan(vehicleNumber, vehicleName, vehicleSize);
                }
                return new Sedan(vehicleNumber, vehicleName, vehicleSize);
            }
            case SUV -> {
                return new SUV(vehicleNumber, vehicleName, vehicleSize, engineType);
            }
            case HATCHBACK -> {
                return new Hatchback(vehicleNumber, vehicleName, vehicleSize, engineType);
            }
            case LARGE_VEHICLE -> {
                return new LargeVehicle(vehicleNumber, vehicleName, vehicleSize, engineType);
            }
        }
        throw new IllegalArgumentException("Invalid vehicle type: " + vehicleType);
    }
}
