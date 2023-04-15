package me.koply.pikap.util;

import java.io.*;
import java.util.HashMap;

public class LightYML extends HashMap<String, String> {

    public LightYML() {
    }

    protected void readYML(File file) {
        try {
            InputStream fis = new FileInputStream(file);
            InputStreamReader isreader = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isreader);

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() == 0 || line.isBlank() || line.startsWith("#")) continue;
                String[] sided = line.split(":");
                this.put(sided[0].trim(), sided[1].trim());
            }

            isreader.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}