package me.koply.pikap.database.model;

import java.sql.Timestamp;

public record Track(int id,
                    String title,
                    String author,
                    String identifier,
                    long duration,
                    int listenedTimes,
                    long lastMillis,
                    Timestamp lastPlayed) {

}
