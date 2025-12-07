package me.koply.pikap.discordrpc;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.activity.Activity;
import de.jcm.discordgamesdk.activity.ActivityType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.koply.pikap.sound.MediaFacade;
import me.koply.pikap.util.ColorHelper;

import java.io.File;
import java.time.Instant;

@Slf4j
public class DiscordRPC {

    public static final File LIB_FOLDER = new File("lib/");

    public final Instant start;
    private final DiscordRPCAudioListener listener;

    @Getter
    @Setter
    private Activity activity;

    @Getter
    private Core core;

    public DiscordRPC(MediaFacade mediaFacade) {
        this.start = Instant.now();
        this.listener = new DiscordRPCAudioListener(this, mediaFacade);
        mediaFacade.registerEventAdapter(listener);
    }

    private DiscordRPCThread rpcThread;
    public void loadAsync() {
        rpcThread = new DiscordRPCThread(this, start);
        rpcThread.start();
    }

    public boolean prepare() {
        File libFile = DownloadNativeDiscordRPC.downloadAndGetLibraryFile(LIB_FOLDER);
        if (libFile == null) {
            System.out.println(ColorHelper.red("DiscordRPC cannot be initialized. Further details in log file."));
            return false;
        }

        Core.init(libFile);

        CreateParams params = new CreateParams();
        params.setClientID(1150793311997657262L);
        params.setFlags(CreateParams.Flags.NO_REQUIRE_DISCORD);

        try {
            core = new Core(params);
            return true;
        } catch (Exception ex) {
            System.out.println(ColorHelper.red("DiscordRPC cannot be initialized. Further details in log file."));
            log.error("DiscordRPC cannot be initialized.", ex);
            return false;
        }
    }

    public Activity createDefaultActivityWithTimestamp() {
        Activity act = createDefaultActivity();
        act.timestamps().setStart(start);
        return act;
    }

    public Activity createDefaultActivity() {
        Activity act = new Activity();
        act.setType(ActivityType.LISTENING);
        act.setDetails("Idle");
        act.assets().setLargeImage("plak2");
        act.assets().setLargeText("Pikap");

        return act;
    }

    public void clearActivity() {
        if (core != null) {
            core.activityManager().clearActivity();
        }
    }

    public void close() {
        try {
            core.close();
            rpcThread.interrupt();
            rpcThread = null;
        } catch (Exception ignored) {
        }
    }

}
