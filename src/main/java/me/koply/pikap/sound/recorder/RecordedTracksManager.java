package me.koply.pikap.sound.recorder;

import me.koply.pikap.api.cli.Console;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static me.koply.pikap.Main.CONFIG;

public class RecordedTracksManager {

    private final List<ReadableTrackFile> trackFiles = new ArrayList<>();

    public RecordedTracksManager() {

    }

    public void readTrackFiles() {
        if (!CONFIG.recordCheck()) {
            Console.info("Record option is not enabled.");
            return;
        }

        String recFolderPath = CONFIG.get("rec_folder");
        if (recFolderPath == null) {
            Console.warn("Recfolder options is not correct.");
            return;
        }

        File folder = new File(recFolderPath);
        if (!folder.isDirectory()) {
            folder.mkdir();
        }

        File[] files = folder.listFiles();
        if (files == null) {
            Console.info("Cannot read the rec_folder files.");
            return;
        }

        Console.debugLog("Recorded track files found: " + files.length);

        for (File file : files) {
            if (file.getName().endsWith(".ptf")) {
                trackFiles.add(new ReadableTrackFile(file));
            }
        }

    }


    public List<ReadableTrackFile> getTrackFiles() {
        return trackFiles;
    }
}