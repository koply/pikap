package me.koply.pikap.database;

public enum Databases {
    SQLITE(SqliteDS.class);

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