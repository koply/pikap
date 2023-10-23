package me.koply.pikap.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileUtil {

    public static String readFile(File file) {
        try (FileInputStream fileInput = new FileInputStream(file)) {
            return readInputStream(fileInput);
        } catch (IOException e) {
            e.printStackTrace();
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
            ex.printStackTrace();
            return "";
        }
    }

    public static void writeFile(File file, String str) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(str);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean isFile(File file) {
        return file != null && file.exists() && file.isFile() && file.canRead();
    }

    public static void createDirectory(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void createNewFile(File file) {
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}