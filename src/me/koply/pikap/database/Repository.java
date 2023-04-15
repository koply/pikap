package me.koply.pikap.database;

public enum Repository {
    SQLITE(SqliteRepository.class);

    public final Class<? extends DBRepository> repositoryClass;
    Repository(Class<? extends DBRepository> repositoryClass) {
        this.repositoryClass = repositoryClass;
    }

    public static Repository fromName(String name) {
        Repository[] repos = Repository.values();
        for (Repository repo : repos) {
            if (name.equalsIgnoreCase(repo.name())) return repo;
        }
        return null;
    }
}