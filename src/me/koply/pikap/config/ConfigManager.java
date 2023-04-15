package me.koply.pikap.config;

import me.koply.pikap.Main;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.util.FileUtil;
import me.koply.pikap.util.LightYML;
import me.koply.pikap.util.Util;

import java.io.File;
import java.io.IOException;

public class ConfigManager extends LightYML {

    // maybe hot changes from commands
    public final File file;

    public ConfigManager(File configFile) {
        this.file = configFile;
    }

    public void createDefault() {
        if (Main.resetConfig|| !FileUtil.isFile(file)) {
            try {
                file.createNewFile();
                FileUtil.writeFile(file, ConfigManager.DEFAULT);
            } catch (IOException ex) {
                ex.printStackTrace();
                Console.warn("PANIC! config.yml file couldn't created/writed.");
            }
        }
    }

    public void initialize() {
        createDefault();
        readYML(file);
    }

    public static final String DEFAULT = "debug: false\n\n" +
                                         "# Default sqlite. just set it to false to disable it\n" +
                                         "# Possible selections: sqlite, disable\n" +
                                         "db: disable\n\n" +
                                         "# Default './data.db'\n" +
                                         "dbfile: ./data.db\n\n" +
                                         "# If you want to save the all logs, you can set this field with file name.\n# Also you have to uncomment the following line.\n" +
                                         "# logfile: disable\n\n" +
                                         "# Search result list count limitor. -1 is unlimited.\nsearchlimitor: -1";

    private Integer searchLimitor;

    public Integer getSearchLimitor() {
        if (searchLimitor != null) return searchLimitor;

        String str = getOrDefault("searchlimitor", null);
        Integer parsed = Util.parseInt(str);
        searchLimitor = parsed == null ? -1 : parsed;
        return searchLimitor;
    }
}