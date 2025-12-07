package me.koply.pikap.discordrpc;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class DownloadResult {

    private final File file;
    private final DownloadStatus status;

    public DownloadResult(File file, DownloadStatus status) {
        this.file = file;
        this.status = status;
    }

    private String message;

}