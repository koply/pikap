package me.koply.pikap.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Slf4j
public class FileHelper {

    public static String readFile(File file) {
        try (FileInputStream fileInput = new FileInputStream(file)) {
            return readInputStream(fileInput);
        } catch (IOException e) {
            log.error("Failed to read file: {}", file, e);
            return "";
        }
    }

    public static String readInputStream(InputStream inputStream) {
        StringBuilder strBuilder = new StringBuilder();
        try (InputStreamReader inputReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(inputReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                strBuilder.append(line).append("\n");
            }

            return strBuilder.toString();
        } catch (Exception ex) {
            log.error("Failed to read input stream", ex);
            return "";
        }
    }

    public static void writeFile(File file, String str) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(str);
        } catch (Exception ex) {
            log.error("Failed to write file: {}", file, ex);
        }
    }

    public static boolean isFile(File file) {
        return file != null && file.exists() && file.isFile() && file.canRead();
    }

    public static boolean createDirectory(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.exists();
    }

    public static boolean createNewFile(File file) {
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                return true;
            } catch (IOException e) {
                log.error("Failed to create file: {}", file, e);
                return false;
            }
        }
        return false;
    }

}