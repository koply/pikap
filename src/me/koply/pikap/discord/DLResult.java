package me.koply.pikap.discord;

import java.io.File;

public class DLResult {

    public DLResult(File file, DLStatus status) {
        this.file = file;
        this.status = status;
    }

    public final File file;
    public final DLStatus status;

    private String message;

    public String getMessage() {
        return message;
    }

    public DLResult setMessage(String message) {
        this.message = message;
        return this;
    }
}