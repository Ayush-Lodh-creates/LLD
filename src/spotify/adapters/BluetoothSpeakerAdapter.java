package spotify.adapters;

import spotify.entities.Song;
import spotify.external_system.BluetoothSpeakerAPI;

public class BluetoothSpeakerAdapter extends IAudioOutput{

    private final BluetoothSpeakerAPI bluetoothSpeakerAPI;

    public BluetoothSpeakerAdapter() {
        this.bluetoothSpeakerAPI = new BluetoothSpeakerAPI();
    }

    @Override
    public void playAudio(Song s) {
        bluetoothSpeakerAPI.playSound(s.getPath());
    }

    @Override
    public void pauseAudio() {
        bluetoothSpeakerAPI.pauseSound();
    }
}
