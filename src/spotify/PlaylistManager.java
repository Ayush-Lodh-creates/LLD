package spotify;

import spotify.entities.Playlist;
import spotify.entities.Song;

import java.util.HashMap;
import java.util.Map;

public class PlaylistManager {

    private static PlaylistManager instance;
    private final Map<String, Playlist> playlists = new HashMap<>();

    private PlaylistManager() {}

    public static PlaylistManager getInstance() {
        if(instance == null) {
            instance = new PlaylistManager();
        }
        return instance;
    }

    public void createPlaylist(String name) {
        if(playlists.containsKey(name)) {
            throw new IllegalArgumentException("Playlist with this name already exists");
        }
        Playlist playlist = new Playlist(name);
        playlists.put(name, playlist);
    }

    public Playlist getPlaylist(String name) {
        if(!playlists.containsKey(name)) {
            throw new IllegalArgumentException("Playlist with this name does not exist");
        }
        return playlists.get(name);
    }

    public void addSong(String playlistName, Song s) {
        Playlist playlist = getPlaylist(playlistName);
        playlist.addSong(s);
    }
}
