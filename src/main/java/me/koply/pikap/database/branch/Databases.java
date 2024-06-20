package me.koply.pikap.database.branch;

import me.koply.pikap.database.api.Database;

// TODO: Change it to AOP style with class annotation
public enum Databases {
    SQLITE(SqliteDB.class);

    public final Class<? extends Database> repositoryClass;
    Databases(Class<? extends Database> repositoryClass) {
        this.repositoryClass = repositoryClass;
    }

    public static Databases fromName(String name) {
        Databases[] repos = Databases.values();
        for (Databases repo : repos) {
            if (name.equalsIgnoreCase(repo.name())) return repo;
        }
        return null;
    }
}