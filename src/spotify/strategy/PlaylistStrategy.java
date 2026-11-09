package spotify.strategy;

import spotify.entities.Playlist;
import spotify.entities.Song;

public interface PlaylistStrategy {

    void setPlaylist(Playlist playlist);
    Song next();
    Song previous();
    boolean hasNext();
    boolean hasPrevious();
}
