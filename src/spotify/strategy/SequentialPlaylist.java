package spotify.strategy;

import spotify.entities.Playlist;
import spotify.entities.Song;

import java.util.ListIterator;

public class SequentialPlaylist implements PlaylistStrategy{

    private ListIterator<Song> iterator;

    @Override
    public void setPlaylist(Playlist playlist) {
        this.iterator = playlist.getSongs().listIterator();
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
