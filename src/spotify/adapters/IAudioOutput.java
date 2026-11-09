package spotify.adapters;

import spotify.entities.Song;

public abstract class IAudioOutput {

    public abstract void playAudio(Song s);
    public abstract void pauseAudio();
}
