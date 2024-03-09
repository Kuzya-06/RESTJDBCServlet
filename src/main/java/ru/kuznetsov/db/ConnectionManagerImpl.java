package ru.kuznetsov.db;


import ru.kuznetsov.exception.DataBaseDriverLoadException;
import ru.kuznetsov.utils.PropertiesUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManagerImpl implements ConnectionManager {
    private static final String DRIVER_CLASS_KEY = "driver_class";
    private static final String URL_KEY = "connection.url";
    private static final String USERNAME_KEY = "connection.username";
    private static final String PASSWORD_KEY = "connection.password";
    private static ConnectionManager instance;

    private ConnectionManagerImpl() {
    }

    public static synchronized ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManagerImpl();
            loadDriver(PropertiesUtil.getProperties(DRIVER_CLASS_KEY));
        }
        return instance;
    }

    private static void loadDriver(String driverClass) {
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new DataBaseDriverLoadException("Database driver not loaded.");
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                PropertiesUtil.getProperties(URL_KEY),
                PropertiesUtil.getProperties(USERNAME_KEY),
                PropertiesUtil.getProperties(PASSWORD_KEY)
        );
    }

}
