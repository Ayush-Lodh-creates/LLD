# Music Player

This case study models the core interactions of a small music player: manage playlists, select an output device, and traverse songs in sequential or shuffled order. The package is named `spotify`, but its scope is local playback orchestration, not a streaming backend.

## Design

[`MusicSystemFacade`](MusicSystemFacade.java) provides `connectDevice()`, `play()`, `pause()`, and `playAll()`. It coordinates three singleton components:

- `DeviceManager` holds the selected audio output.
- `AudioEngine` remembers the most recently played song and forwards play/pause calls.
- `PlaylistManager` stores playlists by name and provides creation, lookup, and song addition.

Playlist management is still accessed through `PlaylistManager`; the facade does not expose every operation.

## Device integration

`DeviceFactory` creates Bluetooth speaker, wired speaker, or headphone adapters. Each adapter translates the common `IAudioOutput` abstraction into its device API's `playSound()` and `pauseSound()` methods. `IAudioOutput` is an abstract class despite its interface-style name.

The adapter boundary keeps the engine independent of individual device APIs. A new output type needs an adapter and a factory mapping. The APIs in `external_system` are stubs that print messages; no audio is decoded or played.

## Playlist traversal

`SequentialPlaylist` iterates over a copy of the playlist's song list. `RandomisedPlaylist` shuffles that copy with a Fisher-Yates-style loop and then creates an iterator. The original playlist order is preserved.

Both implement `PlaylistStrategy`, including forward/backward traversal and boundary checks. `previous()` follows Java `ListIterator` cursor semantics: immediately after `next()`, it returns the same element. It is not a separate playback-history feature.

`playAll()` looks up the playlist, initializes the supplied strategy, and repeatedly calls `next()` and `play()`. It runs synchronously and does not wait for a song to finish. With the current stubs, it simply prints the selected songs in order.

## Example

There is no `Main` in this package. With the corresponding imports, a driver can use:

```java
PlaylistManager playlists = PlaylistManager.getInstance();
playlists.createPlaylist("Evening");
playlists.addSong("Evening", new Song("Track One", "Artist", "track-one.mp3"));
playlists.addSong("Evening", new Song("Track Two", "Artist", "track-two.mp3"));

MusicSystemFacade player = MusicSystemFacade.getInstance();
player.connectDevice(DeviceType.HEADPHONES);
player.playAll(new SequentialPlaylist(), "Evening");
```

## Trade-offs and next steps

Global singleton state keeps this example small but represents only one shared playback session. Singleton initialization and mutable playlists are not thread-safe.

`pause()` compares song names, so songs with the same name are indistinguishable. Calling it before a song has played can dereference a null current song. There is no explicit playing/paused/stopped state, completion callback, queue persistence, or device-disconnection handling.

Playlist copies protect list membership during traversal, but the `Song` objects are still shared and mutable. Preparing either traversal uses O(n) additional space; shuffle also takes O(n) time.

A practical extension would introduce a playback session with explicit state and song identity, then advance the playlist on a completion event instead of a tight loop.
