package me.koply.pikap.database;

import java.lang.reflect.InvocationTargetException;

public class DBFactory {

    public static DBRepository create(Repository choice) {
        try {
            return choice.repositoryClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
            return new SqliteRepository();
        }
    }
}