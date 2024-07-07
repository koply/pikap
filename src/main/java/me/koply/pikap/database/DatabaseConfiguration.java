package me.koply.pikap.database;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class DatabaseConfiguration {
    private String database;
    private String databaseFile;
}