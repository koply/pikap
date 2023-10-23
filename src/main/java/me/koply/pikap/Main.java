package me.koply.pikap;

import com.github.tomaslanger.chalk.Chalk;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.api.cli.command.CommandHandler;
import me.koply.pikap.commands.HelpCommand;
import me.koply.pikap.commands.TrackControlCommands;
import me.koply.pikap.config.ConfigManager;
import me.koply.pikap.database.DBFactory;
import me.koply.pikap.database.Database;
import me.koply.pikap.database.Databases;
import me.koply.pikap.discord.DiscordRPC;
import me.koply.pikap.keyhook.KeyboardListener;
import me.koply.pikap.session.SessionData;
import me.koply.pikap.sound.SoundManager;
import me.koply.pikap.util.FileUtil;
import me.koply.pikap.util.Util;

import java.io.File;

public class Main {

    static {
        //SysErrToSlf4J.redirectSysErr(LoggerFactory.getLogger("SysErr"));
        String logName = Util.getLogName();
        FileUtil.createDirectory(new File("logs/"));
        System.setProperty("org.slf4j.simpleLogger.logFile", "logs/" + logName);
    }

    public static boolean resetConfig = false; // for development
    public static final ConfigManager CONFIG = new ConfigManager(new File("./config.yml"));

    public static Database repository;
    public static final SessionData SESSION = new SessionData();

    public static final SoundManager SOUND_MANAGER = new SoundManager();

    // for DiscordRPC Library
    public static final File LIB_FOLDER = new File("lib/");
    public static final DiscordRPC DISCORD_RPC = new DiscordRPC();

    private static final KeyboardListener KEY_LISTENER = new KeyboardListener();
    private static final CommandHandler COMMAND_HANDLER = new CommandHandler(HelpCommand.class.getPackageName());

    // hello world, ConfigManager initialization, database initialization, SoundManager initialization, SessionDataStore initialization
    public static void main(String[] args) {
        System.out.println(Chalk.on(Constants.BANNER).red() + "\n" + Constants.FIRST_LINE +"\n");

        CONFIG.initialize();

        Databases selectedDatabase = Databases.fromName(CONFIG.getOrDefault("db", ""));
        if (selectedDatabase == null) {
            Console.log("Database not selected.");
        } else {
            repository = DBFactory.create(selectedDatabase);
            boolean success = repository.connect(CONFIG);
            if (!success) {
                Console.warn("PANIC! Database connection isn't established. Check the credentials/file identifies.");
                return;
            }
        }

        SOUND_MANAGER.initialize();

        KEY_LISTENER.registerHook();
        //Console.info("Initializing the Discord RPC module.");
        //DISCORD_RPC.loadAsync();
        //Console.info("Discord RPC initialized.");

        // EventManager.registerListener(new AudioEventDebugger());
        // EventManager.debugListeners();

        // registers the data store's listener object to the EventManager
        SESSION.registerListener();

        COMMAND_HANDLER.registerInstance(TrackControlCommands.getInstance());

        // blocks the main thread
        COMMAND_HANDLER.startNewHandler();
        System.exit(0);
    }
}