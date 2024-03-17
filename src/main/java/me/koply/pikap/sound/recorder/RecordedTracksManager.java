package me.koply.pikap.sound.recorder;

import me.koply.pikap.api.cli.Console;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static me.koply.pikap.Main.CONFIG;

public class RecordedTracksManager {

    private final List<ReadableTrackFile> trackFiles = new ArrayList<>();

    public RecordedTracksManager() {
        if (!CONFIG.recordCheck()) {
            Console.warn("The config entry named as 'record' was not setted to true.");
        }
    }

    public void readTrackFiles() {
        String recFolderPath = CONFIG.get("recfolder");
        if (recFolderPath == null) {
            Console.warn("Record files cannot be read.");
            Console.warn("The config entry named as 'recfolder' was not setted to folder path.");
            return;
        }

        File folder = new File(recFolderPath);
        File[] files = folder.listFiles();
        if (!folder.isDirectory() || files == null) {
            Console.warn("The config entry named as 'recfolder' isn't points a directory.");
            return;
        }

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