package spotify;

import spotify.adapters.IAudioOutput;
import spotify.entities.Song;

public class AudioEngine {

    private Song song;
    private static AudioEngine audioEngine;

    private AudioEngine() {}

    public static AudioEngine getInstance() {
        if(audioEngine == null) {
            audioEngine = new AudioEngine();
        }
        return audioEngine;
    }

    public void play(IAudioOutput audioOutput, Song song) {
        this.song = song;
        audioOutput.playAudio(this.song);
    }

    public void pause(IAudioOutput audioOutput, Song song) {
        if(!this.song.getName().equals(song.getName())) {
            throw new IllegalArgumentException("Song is not currently playing");
        }
        audioOutput.pauseAudio();
    }
}
