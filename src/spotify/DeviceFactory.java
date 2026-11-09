package spotify;

import spotify.adapters.BluetoothSpeakerAdapter;
import spotify.adapters.HeadphonesAdapter;
import spotify.adapters.IAudioOutput;
import spotify.adapters.WiredSpeakerAdapter;
import spotify.enums.DeviceType;

public class DeviceFactory {

    public static IAudioOutput createDevice(DeviceType deviceType) {
        if(deviceType == DeviceType.BLUETOOTH) {
            return new BluetoothSpeakerAdapter();
        } else if(deviceType == DeviceType.WIRED_SPEAKER) {
            return new WiredSpeakerAdapter();
        } else if(deviceType == DeviceType.HEADPHONES) {
            return new HeadphonesAdapter();
        }
        throw new IllegalArgumentException("Unsupported device type");
    }
}
