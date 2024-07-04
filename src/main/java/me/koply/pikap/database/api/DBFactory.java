package me.koply.pikap.database.api;

import me.koply.pikap.database.dao.SqliteDAO;

import java.lang.reflect.InvocationTargetException;

public class DBFactory {

    public static DatabaseAccessObject create(DatabaseType choice) {
        try {
            return choice.repositoryClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
            return new SqliteDAO();
        }
    }
}