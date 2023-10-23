package me.koply.pikap.database;

import java.lang.reflect.InvocationTargetException;

public class DBFactory {

    public static Database create(Databases choice) {
        try {
            return choice.repositoryClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
            return new SqliteDS();
        }
    }
}