package spotify.strategy;

import spotify.entities.Playlist;
import spotify.entities.Song;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class RandomisedPlaylist implements PlaylistStrategy {

    private ListIterator<Song> iterator;

    @Override
    public void setPlaylist(Playlist playlist) {
        List<Song> songs = playlist.getSongs();
        shuffle(songs);
        this.iterator = songs.listIterator();
    }

    private void shuffle(List<Song> songs) {
        Random rnd = new Random();
        for (int i = songs.size() - 1; i > 0; i--)
            Collections.swap(songs, i, rnd.nextInt(i + 1));
    }

    @Override
    public Song next() {
        if(hasNext()) {
            return iterator.next();
        }
        throw new IllegalStateException("No more songs in playlist");
    }

    @Override
    public Song previous() {
        if(hasPrevious()) {
            return iterator.previous();
        }
        throw new IllegalStateException("No previous song in playlist");
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public boolean hasPrevious() {
        return iterator.hasPrevious();
    }
}
