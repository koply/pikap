package me.koply.pikap.discordrpc;

import lombok.extern.slf4j.Slf4j;
import me.koply.pikap.util.FileHelper;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
public class DownloadNativeDiscordRPC {

    private final static String SDK_URL = "https://dl-game-sdk.discordapp.net/2.5.6/discord_game_sdk.zip";

    public static File downloadAndGetLibraryFile(File lib_folder) {
        try {
            DownloadResult result = DownloadNativeDiscordRPC.downloadDiscordLibrary(lib_folder);

            if (result.getStatus() == DownloadStatus.DOWNLOADED || result.getStatus() == DownloadStatus.EXISTS) {
                return result.getFile();
            } else {
                log.warn("Discord's GameSDK cannot be downloaded.");
                log.warn("Cause: {}", result.getMessage());
                return null;
            }
        } catch (IOException | URISyntaxException e) {
            log.warn("Discord's GameSDK cannot be downloaded.");
            log.warn("Cause: {}", e.getMessage());
            return null;
        }
    }

    /*
     * Inspired from github.com/JnCrMx/discord-game-sdk4j/blob/master/examples/DownloadNativeLibrary.java
     */
    private static DownloadResult downloadDiscordLibrary(File destinationFolder) throws IOException, URISyntaxException {
        FileHelper.createDirectory(destinationFolder);

        String name = "discord_game_sdk";
        String suffix;

        String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        String arch = System.getProperty("os.arch").toLowerCase(Locale.ROOT);

        if (osName.contains("windows")) {
            suffix = ".dll";
        } else if (osName.contains("linux")) {
            suffix = ".so";
        } else if (osName.contains("mac")) {
            suffix = ".dylib";
        } else {
            var result = new DownloadResult(null, DownloadStatus.ERROR);
            result.setMessage("Cannot determine the operating system: " + osName);
            return result;
        }

        File check = new File(destinationFolder, name+suffix);
        if (check.exists()) {
            var result = new DownloadResult(check, DownloadStatus.EXISTS);
            result.setMessage("Library file already exists.");
            return result;
        }

        // Some systems report "amd64" (e.g. Windows and Linux), some "x86_64" (e.g. macOS).
        // At this point we need the "x86_64" version, as this one is used in the ZIP.

        if(arch.equals("amd64"))
            arch = "x86_64";

        // Path of Discord's library inside the ZIP
        String zipPath = "lib/"+arch+"/"+name+suffix;

        // Open the URL as a ZipInputStream
        URL downloadUrl = new URI(SDK_URL).toURL();
        HttpURLConnection connection = (HttpURLConnection) downloadUrl.openConnection();
        connection.setRequestProperty("User-Agent", "Pikap");
        ZipInputStream zin = new ZipInputStream(connection.getInputStream());

        // Search for the right file inside the ZIP
        ZipEntry entry;
        while ((entry = zin.getNextEntry()) != null) {
            if (entry.getName().equals(zipPath)) {

                File finishFile = new File(destinationFolder, name+suffix);

                Files.copy(zin, finishFile.toPath());

                zin.close();

                return new DownloadResult(finishFile, DownloadStatus.DOWNLOADED);
            }
            zin.closeEntry();
        }
        zin.close();

        var result = new DownloadResult(null, DownloadStatus.NOT_FOUND);
        result.setMessage("The library file was not found inside the ZIP file.");
        return result;
    }

}