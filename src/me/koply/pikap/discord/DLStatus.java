package me.koply.pikap.discord;

public enum DLStatus {

    DOWNLOADED(0), EXISTS(1), ERROR(2), NOT_FOUND(404);

    final int value;
    DLStatus(int value) {
        this.value = value;
    }

}