package me.koply.pikap.discord;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.koply.pikap.Main;
import me.koply.pikap.api.event.*;
import me.koply.pikap.sound.SoundManager;

import java.time.Instant;

public class RPCEventListener extends EventListenerAdapter {


    private final SoundManager soundManager = SoundManager.getInstance();
    private final DiscordRPC rpc;
    public RPCEventListener(DiscordRPC rpc) {
        this.rpc = rpc;
    }

    @Override
    public void onPlay(PlayEvent e) {
        if (!e.isAddedToQueue) setRPC(e.track);
    }

    @Override
    public void onTrackEnd(TrackEndEvent e) {
        if (soundManager.getQueue().isEmpty()) {
            rpc.setActivity(rpc.createDefaultActivityWithTimestamp());
            rpc.getCore().activityManager().updateActivity(rpc.getActivity());
        }
    }

    @Override
    public void onNextTrack(NextTrackEvent e) {
        setRPC(e.nextTrack);
    }

    @Override
    public void onPlaylist(PlaylistEvent e) {
        if (e.firstTrackStarted) setRPC(e.playlist.getTracks().get(0));
    }

    @Override
    public void onPause(PauseEvent e) {
        rpc.getActivity().assets().setSmallImage("pause-w");
        rpc.getActivity().assets().setSmallText("Music Paused!");
        rpc.getCore().activityManager().updateActivity(rpc.getActivity());
    }

    @Override
    public void onResume(ResumeEvent e) {
        rpc.getActivity().assets().setSmallImage("play-w");
        rpc.getActivity().assets().setSmallText("Music Playing!");
        rpc.getActivity().timestamps().setStart(Instant.now());
        rpc.getActivity().timestamps().setEnd(Instant.now().plusMillis(e.track.getDuration()-e.track.getPosition()));
        rpc.getCore().activityManager().updateActivity(rpc.getActivity());
    }

    private void setRPC(AudioTrack track) {
        AudioTrackInfo info = track.getInfo();
        rpc.getActivity().setDetails(info.title);
        rpc.getActivity().setState(info.author);
        rpc.getActivity().timestamps().setStart(Instant.now());
        rpc.getActivity().timestamps().setEnd(Instant.now().plusMillis(info.length));
        rpc.getActivity().assets().setSmallImage("play-w");
        rpc.getActivity().assets().setSmallText("Music Playing!");
        rpc.getCore().activityManager().updateActivity(rpc.getActivity());
    }
}