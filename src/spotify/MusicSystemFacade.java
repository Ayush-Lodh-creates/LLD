package spotify;

import spotify.adapters.IAudioOutput;
import spotify.entities.Playlist;
import spotify.entities.Song;
import spotify.enums.DeviceType;
import spotify.strategy.PlaylistStrategy;

public class MusicSystemFacade {

    private final AudioEngine audioEngine = AudioEngine.getInstance();
    private final DeviceManager deviceManager = DeviceManager.getInstance();
    private final PlaylistManager playlistManager = PlaylistManager.getInstance();
    private static MusicSystemFacade musicSystemFacade;

    private MusicSystemFacade() {}

    public static MusicSystemFacade getInstance() {
        if (musicSystemFacade == null) {
            musicSystemFacade = new MusicSystemFacade();
        }
        return musicSystemFacade;
    }

    public void connectDevice(DeviceType deviceType) {
        deviceManager.connectDevice(deviceType);
    }

    public void play(Song song) {
        IAudioOutput audioOutput = deviceManager.getDevice();
        audioEngine.play(audioOutput, song);
    }

    public void pause(Song song) {
        IAudioOutput audioOutput = deviceManager.getDevice();
        audioEngine.pause(audioOutput, song);
    }

    public void playAll(PlaylistStrategy strategy, String playlistName) {
        Playlist playlist = playlistManager.getPlaylist(playlistName);
        strategy.setPlaylist(playlist);
        while(strategy.hasNext()) {
            audioEngine.play(deviceManager.getDevice(), strategy.next());
        }
    }
}
