package spotify.adapters;

import spotify.entities.Song;
import spotify.external_system.HeadphonesAPI;

public class HeadphonesAdapter extends IAudioOutput{

    private final HeadphonesAPI headphonesAPI;

    public HeadphonesAdapter() {
        this.headphonesAPI = new HeadphonesAPI();
    }

    @Override
    public void playAudio(Song s) {
        headphonesAPI.playSound(s.getPath());
    }

    @Override
    public void pauseAudio() {
        headphonesAPI.pauseSound();
    }
}
