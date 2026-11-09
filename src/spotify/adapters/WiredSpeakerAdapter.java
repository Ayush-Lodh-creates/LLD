package spotify.adapters;

import spotify.entities.Song;
import spotify.external_system.BluetoothSpeakerAPI;
import spotify.external_system.WiredSpeakerAPI;

public class WiredSpeakerAdapter extends IAudioOutput{

    private WiredSpeakerAPI wiredSpeakerAPI;

    public void setWiredSpeakerAPI() {
        this.wiredSpeakerAPI = new WiredSpeakerAPI();
    }

    @Override
    public void playAudio(Song s) {
        wiredSpeakerAPI.playSound(s.getPath());
    }

    @Override
    public void pauseAudio() {
        wiredSpeakerAPI.pauseSound();
    }
}
