package me.koply.pikap.discordrpc;

import de.jcm.discordgamesdk.activity.Activity;
import de.jcm.discordgamesdk.activity.ActivityType;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class DiscordRPCThread extends Thread {

    private final DiscordRPC discordRPC;
    private final Instant start;
    public DiscordRPCThread(DiscordRPC discordRPC, Instant start) {
        this.discordRPC = discordRPC;
        this.start = start;
    }

    @Override
    public void run() {
        var activity = new Activity();
        activity.setType(ActivityType.LISTENING);
        activity.setDetails("Idle");
        activity.timestamps().setStart(start);
        activity.assets().setLargeImage("plak2");
        activity.assets().setLargeText("Pikap");

        var core = discordRPC.getCore();
        core.activityManager().updateActivity(activity);

        try {
            while (core != null) {
                core.runCallbacks();
                Thread.sleep(1024L);
            }
        } catch (Exception ex) {
            log.error("DiscordRPCThread error", ex);
        }
    }

}
