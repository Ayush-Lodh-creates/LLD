package parking_lot.entities.vehicle;

import parking_lot.enums.EngineType;

public class ElectricSedan extends Sedan {

    public ElectricSedan(String vehicleNumber, String vehicleName, Integer vehicleSize) {
        super(vehicleNumber, vehicleName, vehicleSize, EngineType.ELECTRIC);
    }
}
