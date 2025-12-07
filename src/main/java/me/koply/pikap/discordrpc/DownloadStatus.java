package me.koply.pikap.discordrpc;

public enum DownloadStatus {

    DOWNLOADED(0), EXISTS(1), ERROR(2), NOT_FOUND(404);

    final int value;
    DownloadStatus(int value) {
        this.value = value;
    }

}