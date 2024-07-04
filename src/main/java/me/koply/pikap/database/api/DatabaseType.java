package me.koply.pikap.database.api;

import me.koply.pikap.database.dao.SqliteDAO;

// TODO: Change it to AOP style with class annotation
public enum DatabaseType {
    SQLITE(SqliteDAO.class);

    public final Class<? extends DatabaseAccessObject> repositoryClass;
    DatabaseType(Class<? extends DatabaseAccessObject> repositoryClass) {
        this.repositoryClass = repositoryClass;
    }

    public static DatabaseType fromName(String name) {
        DatabaseType[] repos = DatabaseType.values();
        for (DatabaseType repo : repos) {
            if (name.equalsIgnoreCase(repo.name())) return repo;
        }
        return null;
    }
}