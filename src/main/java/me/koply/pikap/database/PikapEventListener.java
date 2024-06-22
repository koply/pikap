package me.koply.pikap.database;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.api.event.*;
import me.koply.pikap.database.api.Database;
import me.koply.pikap.database.model.PlayedPlaylist;
import me.koply.pikap.database.model.Playlist;
import me.koply.pikap.database.model.Track;
import me.koply.pikap.sound.SoundManager;

import java.sql.Timestamp;
import java.time.Instant;

public class PikapEventListener extends EventListenerAdapter {

    private final Database db;
    public PikapEventListener(Database db) {
        this.db = db;
    }

    @Override
    public void onPlay(PlayEvent e) {
        saveNewTrack(e.track, true, null);
    }

    @Override
    public void onPlaylist(PlaylistEvent e) {
        if (e.firstTrackStarted) {
            saveNewTrack(e.playlist.getTracks().get(0), true, null);
        }

        // https://www.youtube.com/watch?v=bzrSweHAbIk&list=PLqjIyifcLPWGrMsrjTPvX0oBYPAXpPoMV
        String order = SoundManager.getOrder();
        if (order == null) return;


        String[] first = order.split("\\?");
        String[] parameters = first[1].split("&");

        String identifier = "";
        for (String parameter : parameters) {
            if (parameter.startsWith("list")) {
                identifier = parameter.substring(5);
                break;
            }
        }
        if (identifier.isEmpty()) {
            Console.debugLog("Identifier is empty. Should be investigate...");
            return;
        }

        AudioPlaylist audioPlaylist = e.playlist;

        Playlist playlist = db.queryPlaylistByIdentifier(identifier);
        if (playlist == null) {
            // order because playlists can only be played with order
            playlist = new Playlist(audioPlaylist, identifier, e.duration);
            playlist.setCreatedAt(Timestamp.from(Instant.now()));
            db.createPlaylist(playlist);
        }
        String foundIds = playlist.getTrackIdsString() != null && !playlist.getTrackIdsString().isEmpty() ? playlist.getTrackIdsString() : "";

        StringBuilder trackIds = new StringBuilder(foundIds);
        Track firstTrack = null;
        for (AudioTrack track : audioPlaylist.getTracks()) {
            Track savedTrack = saveNewTrack(track, false, playlist.getId());
            if (firstTrack == null) firstTrack = savedTrack;
            trackIds.append(savedTrack.getId()).append(",");
        }
        playlist.setTrackIds(trackIds.toString());
        db.updatePlaylist(playlist);


        PlayedPlaylist playedPlaylist = new PlayedPlaylist(playlist, firstTrack);
        playedPlaylist.setPlayedAt(playlist.getCreatedAt());

        db.createPlayedPlaylist(playedPlaylist);
    }

    @Override
    public void onNextTrack(NextTrackEvent e) {
        if (e.reason == NextTrackEvent.Reason.NEXT && e.pastTrack != null) {
            Track pastTrack = db.queryTrackByIdentifier(e.pastTrack.getIdentifier());
            pastTrack.setLastMillis(e.pastTrack.getPosition());
            db.updateTrack(pastTrack);
        }
        saveNewTrack(e.nextTrack, true, null);
    }

    @Override
    public void onTrackEnd(TrackEndEvent e) {
        Track track = db.queryTrackByIdentifier(e.endTrack.getIdentifier());
        if (track == null) return;
        track.setLastMillis(e.endTrack.getPosition());
        db.updateTrack(track);
    }

    @Override
    public void onPause(PauseEvent e) {
        Track track = db.queryTrackByIdentifier(e.track.getIdentifier());
        if (track == null) return;
        track.setLastMillis(e.track.getPosition());
        db.updateTrack(track);
    }

    private Track saveNewTrack(AudioTrack audioTrack, boolean isPlayed, Integer playlistId) {
        Track track = db.queryTrackByIdentifier(audioTrack.getIdentifier());
        boolean create = false;
        if (track == null) {
            track = new Track(audioTrack.getInfo());
            create = true;
        }

        if (playlistId != null) {
            track.addPlaylistId(playlistId);
        }

        if (isPlayed) {
            track.increaseListenedTimes();
            track.setLastMillis(0);

            // Benchmark with this: new Timestamp(System.currentTimeMillis())
            track.setLastPlayed(Timestamp.from(Instant.now()));

            if (create) track.setFirstPlayed(track.getLastPlayed());
        }

        if (create) db.createTrack(track);
        else db.updateTrack(track);
        return track;
    }
}
