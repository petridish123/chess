package dataaccess;

import model.UserData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySqlUserDao implements UserDataAccess {
    private static final String DATABASE_NAME;
    private static final String USER;
    private static final String PASSWORD;
    private static final String CONNECTION_URL;
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) {
                    throw new Exception("Unable to load db.properties");
                }
                Properties props = new Properties();
                props.load(propStream);
                DATABASE_NAME = props.getProperty("db.name");
                USER = props.getProperty("db.user");
                PASSWORD = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                CONNECTION_URL = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }


    public MySqlUserDao() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME; // also create table games
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        try { // creates my table
            var statement =    """ 
                        CREATE TABLE IF NOT EXISTS users (
                            username varchar(255) PRIMARY KEY,
                            password varchar(255) PRIMARY KEY,
                            email varchar(255),
                            authtoken varchar(255),
                            PRIMARY KEY (username)
                        )"""; // creates the table
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        }   catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            conn.setCatalog(DATABASE_NAME); //connects to my database
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage()); // tells me if a connection has been made
        }
    }

    @Override
    public UserData getUserData(String username) throws DataAccessException {
        var statement = "SELECT * FROM users WHERE username = ?";
        try (var conn = getConnection()){
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String email = resultSet.getString("email");
                        String password = resultSet.getString("password");
                        return new UserData(username, password, email);
                    }
                }
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {

    }

    @Override
    public void clear() {

    }
}
