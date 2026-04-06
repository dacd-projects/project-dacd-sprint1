package org.ulpgc.dacd.persistence;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:aviation.db";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL);
    }
}
