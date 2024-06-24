package me.koply.pikap;

import com.github.tomaslanger.chalk.Chalk;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.api.cli.command.CommandHandler;
import me.koply.pikap.commands.HelpCommand;
import me.koply.pikap.commands.TrackControlCommands;
import me.koply.pikap.config.ConfigManager;
import me.koply.pikap.database.PikapEventListener;
import me.koply.pikap.database.api.DBFactory;
import me.koply.pikap.database.api.Database;
import me.koply.pikap.database.branch.Databases;
import me.koply.pikap.discord.DiscordRPC;
import me.koply.pikap.event.EventManager;
import me.koply.pikap.keyhook.KeyboardListener;
import me.koply.pikap.session.SessionData;
import me.koply.pikap.sound.SoundManager;
import me.koply.pikap.sound.recorder.RecordedTracksManager;
import me.koply.pikap.test.AudioEventDebugger;

public class Main {

    static {
        /* todo make that log system
        SysErrToSlf4J.redirectSysErr(LoggerFactory.getLogger("SysErr"));
        String logName = Util.getLogName();
        FileUtil.createDirectory(Constants.LOGS_FOLDER);
        System.setProperty("org.slf4j.simpleLogger.logFile", "logs/" + logName); */
    }

    public static final boolean resetConfig = false; // for development
    public static final ConfigManager CONFIG = new ConfigManager(Constants.CONFIG_FILE);
    public static final RecordedTracksManager RECORD_MANAGER = new RecordedTracksManager();

    public static final SessionData SESSION = new SessionData();

    public static final SoundManager SOUND_MANAGER = new SoundManager();

    private static final KeyboardListener KEY_LISTENER = new KeyboardListener();
    private static final CommandHandler COMMAND_HANDLER = new CommandHandler(HelpCommand.class.getPackageName());

    private static Database repository;

    // hello world, ConfigManager initialization, database initialization, SoundManager initialization, SessionDataStore initialization
    public static void main(String[] args) {
        System.out.println(Chalk.on(Constants.BANNER).red() + "\n" + Constants.FIRST_LINE +"\n");

        boolean configStatus = CONFIG.initialize();
        if (!configStatus) {
            Console.warn(Constants.PREFIX + "Probably you're using an old version's config.yml file. Please delete the config.yml and restart Pikap.");
            System.exit(1);
        }

        Databases selectedDatabase = Databases.fromName(CONFIG.getOrDefault("db", ""));
        if (selectedDatabase == null) {
            Console.debugLog("Database not selected.");
        } else {
            repository = DBFactory.create(selectedDatabase);
            boolean success = repository.connect(CONFIG);
            if (!success) {
                Console.warn("PANIC! Database connection isn't established. Check the credentials/file identifies.");
                return;
            } else {
                EventManager.registerListener(new PikapEventListener(repository));
            }
        }

        if (CONFIG.entryCheckIgnoreCase("record", "true", "yes")) {
            RECORD_MANAGER.readTrackFiles();
        }

        SOUND_MANAGER.initialize();

        // TODO: Windows Error
        // KEY_LISTENER.registerHook();

        Console.debugLog("Initializing the Discord RPC module.");
        if (DiscordRPC.getInstance().prepare()) {
            DiscordRPC.getInstance().loadAsync();
            Console.debugLog("Discord RPC initialized.");
        } else {
            Console.debugLog("Discord RPC is not initialized. Disabling module...");
        }

        if (CONFIG.isDebug()) {
            EventManager.registerListener(new AudioEventDebugger());
        }
        // EventManager.debugListeners();

        // registers the listener of the data store to the EventManager
        SESSION.registerListener();

        COMMAND_HANDLER.registerInstance(TrackControlCommands.getInstance());

        // blocks the main thread
        COMMAND_HANDLER.startNewHandler();
        System.exit(0);
    }

    public static Database getRepository() {
        return repository;
    }
}