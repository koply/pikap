package me.koply.pikap.api.cli.command;

public enum ReturnType {
    VOID, BOOLEAN, UNKNOWN;

    public static ReturnType getFromType(Class<?> type) {
        return type == Void.TYPE ? VOID : type == Boolean.TYPE ? BOOLEAN : UNKNOWN;
    }
}
