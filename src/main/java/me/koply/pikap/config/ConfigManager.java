package me.koply.pikap.config;

import me.koply.pikap.Constants;
import me.koply.pikap.Main;
import me.koply.pikap.api.cli.Console;
import me.koply.pikap.util.StringUtil;
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
                File defaultConfig = new File(Main.class.getResource("/config.yml").getFile());
                file.createNewFile();

                FileUtil.writeFile(file, FileUtil.readFile(defaultConfig));
            } catch (IOException ex) {
                ex.printStackTrace();
                Console.warn("PANIC! config.yml file couldn't created/writed.");
            }
        }
    }

    public boolean initialize() {
        createDefault();
        readYML(file);

        if (get("maximum_volume") == null) {
            return false;
        }

        String debugValue = get("debug");
        if (debugValue != null) {
            debug = debugValue.equalsIgnoreCase("true");
        }

        String str = getOrDefault("search_limitor", null);
        Integer searchLimitorInt = Util.parseInt(str);
        searchLimitor = searchLimitorInt == null ? -1 : searchLimitorInt;

        Integer queuePagerInt = Util.parseInt(getOrDefault("queue_pager", null));
        queuePager = queuePagerInt == null ? -1 : queuePagerInt;
        return true;
    }

    private boolean debug = true;
    public boolean isDebug() {
        return debug;
    }

    private int searchLimitor = -1;
    public int getSearchLimitor() {
        return searchLimitor;
    }

    private int queuePager;
    public int getQueuePager() {
        return queuePager;
    }

    public boolean recordCheck() {
        String entry = get("record");
        return entry != null && (StringUtil.anyEqualsIgnoreCase(entry, "true", "yes", "on", "okey"));
    }

    public boolean entryCheckIgnoreCase(String entry, String...values) {
        String ent = get(entry);
        return ent != null && (StringUtil.anyEqualsIgnoreCase(ent, values));
    }

    public boolean entryCheck(String entry, String...values) {
        String ent = get(entry);
        return ent != null && (StringUtil.anyEquals(ent, values));
    }
}