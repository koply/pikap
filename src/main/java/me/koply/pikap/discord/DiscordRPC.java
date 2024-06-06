package me.koply.pikap.discord;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.activity.Activity;
import de.jcm.discordgamesdk.activity.ActivityType;
import me.koply.pikap.Main;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.event.EventManager;
import me.koply.pikap.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;

public class DiscordRPC implements Runnable {

    private static final Logger log = LoggerFactory.getLogger("DiscordRPC");

    public final Instant start;
    private final RPCTrackListener listener;

    public DiscordRPC() {
        start = Instant.now();
        listener = new RPCTrackListener(this);
    }

    public void loadAsync() {
        Thread thread = new Thread(this);
        thread.start();

        EventManager.registerListener(listener);
    }

    public void clearActivity() {
        if (core != null) {
            core.activityManager().clearActivity();
        }
    }

    private CreateParams params;

    private Core core;
    public Core getCore() {
        return core;
    }

    private Activity activity;
    public Activity getActivity() {
        return activity;
    }
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Activity createDefaultActivityWithTimestamp() {
        Activity act = createDefaultActivity();
        act.timestamps().setStart(start);
        return act;
    }

    public Activity createDefaultActivity() {
        Activity act = new Activity();
        act.setType(ActivityType.LISTENING);
        activity.setDetails("Idle");
        activity.assets().setLargeImage("plak2");
        activity.assets().setLargeText("Pikap");

        return act;
    }

    @Override
    public void run() {
        File libFile = getLibraryFile();
        if (libFile == null) {
            Console.prefixln("DiscordRPC cannot be initialized. Further details in log file.");
            return;
        }

        Core.init(libFile);

        params = new CreateParams();
        params.setClientID(1150793311997657262L);
        params.setFlags(CreateParams.getDefaultFlags());

        core = new Core(params);
        activity = new Activity();
        activity.setType(ActivityType.LISTENING);
        activity.setDetails("Idle");
        //activity.setState("and having fun");

        // Setting a start time causes an "elapsed" field to appear
        activity.timestamps().setStart(start);


        // We are at a party with 10 out of 100 people.
        //activity.party().size().setMaxSize(100);
        //activity.party().size().setCurrentSize(10);

        // Make a "cool" image show up
        activity.assets().setLargeImage("plak2");
        activity.assets().setLargeText("Pikap");

        // Setting a join secret and a party ID causes an "Ask to Join" button to appear
        // activity.party().setID("Party!");
        // activity.secrets().setJoinSecret("Join!");

        core.activityManager().updateActivity(activity);

        try {
            while (core != null) {
                core.runCallbacks();

                Thread.sleep(1024L);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static File getLibraryFile() {
        try {
            DLResult result = LibraryDL.downloadDiscordLibrary(Main.LIB_FOLDER);

            FileUtil.createDirectory(Main.LIB_FOLDER);

            if (result.status == DLStatus.DOWNLOADED || result.status == DLStatus.EXISTS) {
                return result.file;
            } else {
                log.warn("Discord's GameSDK cannot be downloaded.");
                log.warn("Cause: " + result.getMessage());
                return null;
            }
        } catch (IOException | URISyntaxException e) {
            log.warn("Discord's GameSDK cannot be downloaded.");
            log.warn("Cause: " + e.getMessage());
            e.printStackTrace();

            return null;
        }

    }

}