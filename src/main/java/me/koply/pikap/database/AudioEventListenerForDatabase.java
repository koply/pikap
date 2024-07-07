package me.koply.pikap.database;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.api.event.*;
import me.koply.pikap.database.model.PlayedPlaylist;
import me.koply.pikap.database.model.Playlist;
import me.koply.pikap.database.model.Track;
import me.koply.pikap.event.AudioListenerAdapter;
import me.koply.pikap.event.EventHandler;
import me.koply.pikap.sound.SoundManager;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class AudioEventListenerForDatabase extends AudioListenerAdapter {

    // todo database access object
    private final DatabaseAccessDelegate db;
    public AudioEventListenerForDatabase(DatabaseAccessDelegate db) {
        this.db = db;
    }

    @EventHandler
    @Override
    public void onPlay(PlayEvent e) {
        saveNewTrackAsync(e.track);
    }

    @EventHandler
    @Override
    public void onPlaylist(PlaylistEvent e) {
        if (e.firstTrackStarted) {
            saveNewTrackAsync(e.playlist.getTracks().get(0));
        }

        // https://www.youtube.com/watch?v=bzrSweHAbIk&list=PLqjIyifcLPWGrMsrjTPvX0oBYPAXpPoMV
        String order = SoundManager.getOrder();
        if (order == null) return;

        String[] first = order.split("\\?");
        String[] parameters = first[1].split("&");

        String identifier = Arrays.stream(parameters).filter(parameter -> parameter.startsWith("list")).findFirst().map(parameter -> parameter.substring(5)).orElse("");
        if (identifier.isEmpty()) {
            Console.debugLog("Identifier is empty. Should be investigate...");
            return;
        }

        AudioPlaylist audioPlaylist = e.playlist;

        db.get().getPlaylistDAO().fetchIdentifierAsync(identifier).thenAcceptAsync((pl) -> {
            if (pl == null) {
                Playlist temp = new Playlist(audioPlaylist, identifier, e.duration);
                temp.setCreatedAt(Timestamp.from(Instant.now()));
                db.get().getPlaylistDAO().upsertAsync(temp).thenAcceptAsync((Void) -> registerPlaylist(temp, audioPlaylist));
            } else {
                registerPlaylist(pl, audioPlaylist);
            }
        });
    }

    private void registerPlaylist(Playlist playlist, AudioPlaylist audioPlaylist) {
        List<Integer> foundIds = Arrays.stream(playlist.getTrackIds()).boxed().collect(Collectors.toList());

        Track firstTrack = null;
        for (AudioTrack track : audioPlaylist.getTracks()) {
            Track savedTrack = saveNewTrackSync(track, playlist.getId());
            if (firstTrack == null) firstTrack = savedTrack;
            if (!foundIds.contains(savedTrack.getId())) {
                foundIds.add(savedTrack.getId());
            }
        }
        playlist.setTrackIds(foundIds);
        Track finalFirstTrack = firstTrack;
        db.get().getPlaylistDAO().updateAsync(playlist).thenAcceptAsync((Void) -> {
            PlayedPlaylist playedPlaylist = new PlayedPlaylist(playlist, finalFirstTrack);
            playedPlaylist.setPlayedAt(playlist.getCreatedAt());

            db.get().getPlayedPlaylistDAO().upsertAsync(playedPlaylist);
        });
    }

    @EventHandler
    @Override
    public void onNextTrack(NextTrackEvent e) {
        if (e.reason == NextTrackEvent.Reason.NEXT && e.pastTrack != null) {
            db.get().getTrackDAO().fetchTrackByIdentifierAsync(e.pastTrack.getIdentifier()).thenAcceptAsync((track) -> {
                track.setLastMillis(e.pastTrack.getPosition());
                db.get().getTrackDAO().upsertAsync(track);
            });

        }
        saveNewTrackAsync(e.nextTrack);
    }

    @EventHandler
    @Override
    public void onTrackEnd(TrackEndEvent e) {
        db.get().getTrackDAO().fetchTrackByIdentifierAsync(e.endTrack.getIdentifier()).thenAccept((track) -> {
            if (track == null) return;
            track.setLastMillis(e.endTrack.getPosition());
            db.get().getTrackDAO().upsertAsync(track);
        });

    }

    @EventHandler
    @Override
    public void onPause(PauseEvent e) {
        db.get().getTrackDAO().fetchTrackByIdentifierAsync(e.track.getIdentifier()).thenAccept((track) -> {
            if (track == null) return;
            track.setLastMillis(e.track.getPosition());
            db.get().getTrackDAO().upsertAsync(track);
        });
    }

    private Track saveNewTrackSync(AudioTrack audioTrack, Integer playlistId) {
        Track track;
        try {
            track = db.get().getTrackDAO().fetchTrackByIdentifierAsync(audioTrack.getIdentifier()).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        boolean trackFound = newTrackInformationSetter(track, audioTrack, playlistId, false);

        try {
            if (trackFound) db.get().getTrackDAO().updateAsync(track).wait();
            else db.get().getTrackDAO().insertAsync(track).wait();
            return track;
        } catch (Exception ex) {
            return track;
        }
    }

    private void saveNewTrackAsync(AudioTrack audioTrack) {
        db.get().getTrackDAO().fetchTrackByIdentifierAsync(audioTrack.getIdentifier()).thenAcceptAsync((track) -> {
            boolean trackFound = newTrackInformationSetter(track, audioTrack, null, true);
            if (trackFound) db.get().getTrackDAO().updateAsync(track);
            else db.get().getTrackDAO().insertAsync(track);
        });
    }

    private boolean newTrackInformationSetter(Track track, AudioTrack audioTrack, Integer playlistId, boolean isPlayed) {
        boolean trackFound = true;
        if (track == null) {
            track = new Track(audioTrack.getInfo());
            trackFound = false;
        }

        if (playlistId != null) {
            track.addPlaylistId(playlistId);
        }

        if (isPlayed) {
            track.increaseListenedTimes();
            track.setLastMillis(0);

            // Benchmark with this: new Timestamp(System.currentTimeMillis())
            track.setLastPlayed(Timestamp.from(Instant.now()));

            if (!trackFound) track.setFirstPlayed(track.getLastPlayed());
        }
        return trackFound;
    }
}
