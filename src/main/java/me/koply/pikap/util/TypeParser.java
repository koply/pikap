package me.koply.pikap.util;

public class TypeParser {

    @SuppressWarnings("unchecked")
    public static <T> T parse(String object, Class<T> type) {
        if (type == String.class) {
            return (T) object;
        } else if (type == Integer.class) {
            return (T) parseInt(object);
        } else if (type == Boolean.class) {
            return (T) parseBoolean(object);
        } else if (type == Long.class) {
            return (T) parseLong(object);
        } else if (type == Float.class) {
            return (T) parseFloat(object);
        } else if (type == Double.class) {
            return (T) parseDouble(object);
        } else {
            throw new UnsupportedOperationException("Unsupported type: " + type);
        }
    }

    public static Integer parseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Long parseLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Float parseFloat(String str) {
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Double parseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Boolean parseBoolean(String str) {
        try {
            return Boolean.parseBoolean(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}