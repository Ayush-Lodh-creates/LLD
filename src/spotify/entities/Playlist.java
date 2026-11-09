package spotify.entities;

import java.util.ArrayList;
import java.util.List;

public class Playlist {

    String name;
    private final List<Song> songs = new ArrayList<>();

    public Playlist(String name) {
        this.name = name;
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public void removeSong(Song song) {
        if(songs.remove(song)) {
            return;
        }
        throw new IllegalArgumentException("Song not found in playlist");
    }

    public List<Song> getSongs() {
        return new ArrayList<>(songs);
    }
}
