package parking_lot.entities.vehicle;

import parking_lot.enums.VehicleType;
import parking_lot.enums.EngineType;

public abstract class Vehicle {

    private String vehicleNumber;
    private String vehicleName;
    private VehicleType vehicleType;
    private Integer vehicleSize;
    private EngineType engineType;

    protected Vehicle(String vehicleNumber, String vehicleName, VehicleType vehicleType, Integer vehicleSize, EngineType engineType) {
        this.vehicleNumber = vehicleNumber;
        this.vehicleName = vehicleName;
        this.vehicleType = vehicleType;
        this.vehicleSize = vehicleSize;
        this.engineType = engineType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getVehicleSize() {
        return vehicleSize;
    }

    public void setVehicleSize(Integer vehicleSize) {
        this.vehicleSize = vehicleSize;
    }
    public EngineType getEngineType() {
        return engineType;
    }

    public void setEngineType(EngineType engineType) {
        this.engineType = engineType;
    }
}
