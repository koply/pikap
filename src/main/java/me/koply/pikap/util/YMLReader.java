package me.koply.pikap.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class YMLReader {
    public static boolean readTo(Path path, Map<String, String> map) {
        try (BufferedReader reader = Files.newBufferedReader(path)){
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty() || line.isBlank() || line.startsWith("#")) continue;
                String[] split = line.split(":");
                map.put(split[0].trim(), split[1].trim());
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}