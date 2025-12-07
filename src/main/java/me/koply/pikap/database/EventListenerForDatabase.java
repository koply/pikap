package me.koply.pikap.database;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.koply.pikap.database.repository.TrackRepository;

public class EventListenerForDatabase extends AudioEventAdapter {

    public EventListenerForDatabase(TrackRepository trackRepository) {

    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {

    }
}
