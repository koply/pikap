package me.koply.pikap.util;

import java.util.*;

public class ArgsHelper {

    @SafeVarargs
    public static <K> boolean anyContains(Map<K, ?> map, K... keys) {
        for (K key : keys) {
            if (map.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    @SafeVarargs
    public static <K, V> Optional<V> getAny(Map<K, V> map, K... keys) {
        V val;
        for (K key : keys) {
            if ((val = map.get(key)) != null) return Optional.of(val);
        }
        return Optional.empty();
    }

    public static String[][] parseArgsString(String[] parts) {
        List<String[]> result = new ArrayList<>();

        String currentKey = null;
        StringBuilder currentValue = new StringBuilder();

        for (int i = 1; i < parts.length; i++) {
            String token = parts[i];

            if (token.startsWith("--") || token.startsWith("-")) {
                if (currentKey != null) {
                    result.add(new String[] {
                            currentKey,
                            !currentValue.isEmpty() ? currentValue.toString().trim() : null
                    });
                    currentValue.setLength(0);
                }
                currentKey = token;
            } else {
                if (currentKey == null) {
                    continue;
                }
                currentValue.append(token).append(" ");
            }
        }

        if (currentKey != null) {
            result.add(new String[] {
                    currentKey,
                    !currentValue.isEmpty() ? currentValue.toString().trim() : null
            });
        }

        return result.toArray(new String[0][0]);
    }

    public static Map<String, String> parseArgs(String[] tokens) {
        Map<String, String> resultMap = new LinkedHashMap<>();

        String currentKey = null;
        List<String> currentValueParts = new ArrayList<>();

        for (String token : tokens) {
            if (token.startsWith("--") || token.startsWith("-")) {
                if (currentKey != null) {
                    resultMap.put(currentKey, currentValueParts.isEmpty() ? null : String.join(" ", currentValueParts));
                }
                currentKey = token;
                currentValueParts.clear();
            } else if (currentKey != null) {
                currentValueParts.add(token);
            }
        }

        if (currentKey != null) {
            resultMap.put(currentKey, currentValueParts.isEmpty() ? null : String.join(" ", currentValueParts));
        }

        return resultMap;
    }

}