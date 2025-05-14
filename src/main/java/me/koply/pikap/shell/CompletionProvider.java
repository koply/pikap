package me.koply.pikap.shell;

import java.util.Map;

public interface CompletionProvider {
    Map<String, String[]> getCompletions();
}