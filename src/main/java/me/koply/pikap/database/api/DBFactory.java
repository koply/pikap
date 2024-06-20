package me.koply.pikap.database.api;

import me.koply.pikap.database.branch.Databases;
import me.koply.pikap.database.branch.SqliteDB;

import java.lang.reflect.InvocationTargetException;

public class DBFactory {

    public static Database create(Databases choice) {
        try {
            return choice.repositoryClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
            return new SqliteDB();
        }
    }
}