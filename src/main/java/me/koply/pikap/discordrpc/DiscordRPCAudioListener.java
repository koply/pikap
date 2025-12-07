package me.koply.pikap.discordrpc;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.koply.pikap.sound.MediaFacade;

import java.time.Instant;

public class DiscordRPCAudioListener extends AudioEventAdapter {

    private final MediaFacade mediaFacade;
    private final DiscordRPC rpc;
    public DiscordRPCAudioListener(DiscordRPC rpc, MediaFacade mediaFacade) {
        this.rpc = rpc;
        this.mediaFacade = mediaFacade;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        super.onTrackEnd(player, track, endReason);
        if (!endReason.mayStartNext) {
            rpc.setActivity(rpc.createDefaultActivityWithTimestamp());
            rpc.getCore().activityManager().updateActivity(rpc.getActivity());
        }
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        super.onTrackStart(player, track);
        setRPC(track);
    }

    private void setRPC(AudioTrack track) {
        if (rpc.getActivity() == null) {
            rpc.setActivity(rpc.createDefaultActivity());
        }

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
