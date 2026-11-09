package spotify;

import spotify.adapters.IAudioOutput;
import spotify.enums.DeviceType;

public class DeviceManager {

    private IAudioOutput audioOutput;
    private static DeviceManager instance;

    private DeviceManager() {}

    public static DeviceManager getInstance() {
        if(instance == null) {
            instance = new DeviceManager();
        }
        return instance;
    }

    public IAudioOutput getDevice() {
        if(audioOutput == null) {
            throw new IllegalArgumentException("No device has been set. Please set a device before trying to get it.");
        }
        return audioOutput;
    }

    public void connectDevice(DeviceType deviceType) {
        audioOutput = DeviceFactory.createDevice(deviceType);
    }
}
