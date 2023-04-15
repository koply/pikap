package me.koply.pikap;

import com.github.tomaslanger.chalk.Chalk;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.api.cli.command.CommandHandler;
import me.koply.pikap.commands.HelpCommand;
import me.koply.pikap.commands.TrackControlCommands;
import me.koply.pikap.config.ConfigManager;
import me.koply.pikap.database.DBFactory;
import me.koply.pikap.database.DBRepository;
import me.koply.pikap.database.Repository;
import me.koply.pikap.event.EventManager;
import me.koply.pikap.keyhook.KeyboardListener;
import me.koply.pikap.sound.SoundManager;
import me.koply.pikap.test.AudioEventDebugger;

import java.io.File;

public class Main {

    static {
        System.setProperty("org.slf4j.simpleLogger.logFile", "pikap.log");
    }

    public static boolean resetConfig = false; // for development
    public static SoundManager soundManager;
    public static DBRepository database;
    public static final ConfigManager CONFIG = new ConfigManager(new File("./config.yml"));

    private static final KeyboardListener KEY_LISTENER = new KeyboardListener();
    private static final CommandHandler COMMAND_HANDLER = new CommandHandler(HelpCommand.class.getPackageName());

    // private static final SessionDataStore DATA_STORE = new SessionDataStore();

    // hello world, ConfigManager initialization, database initialization, SoundManager initialization
    public static void main(String[] args) {
        System.out.println(Chalk.on(Constants.BANNER).red() + "\n" + Constants.FIRST_LINE +"\n");

        CONFIG.initialize();

        Repository selectedDatabase = Repository.fromName(CONFIG.get("db"));
        if (selectedDatabase == null) {
            //Console.info("Database not selected.");
        } else {
            database = DBFactory.create(selectedDatabase);
            boolean success = database.connect(CONFIG);
            if (!success) {
                Console.warn("PANIC! Database connection isn't established. Check the credentials/file identifies.");
                return;
            }
        }

        soundManager = new SoundManager();
        KEY_LISTENER.registerHook();

        EventManager.registerListener(new AudioEventDebugger());
        //EventManager.debugListeners();

        COMMAND_HANDLER.registerInstance(TrackControlCommands.getInstance());

        // blocks the main thread
        COMMAND_HANDLER.startNewHandler();
        System.exit(0);
    }
}