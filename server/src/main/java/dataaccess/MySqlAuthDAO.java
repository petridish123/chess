package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySqlAuthDAO implements AuthTokenDataAccess{

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


    public MySqlAuthDAO() throws DataAccessException {
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
                        CREATE TABLE IF NOT EXISTS authdata (
                            username varchar(255) PRIMARY KEY,
                            authtoken varchar(255) NOT NULL,
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
    public AuthData getAuthData(String authToken) throws DataAccessException {
        var statement = "SELECT * FROM authdata WHERE authtoken = ?";
        try (var conn = getConnection()){
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                try (var result = preparedStatement.executeQuery()) {
                    if (result.next()){
                        var username = result.getString("username");
                        return new AuthData(authToken, username);
                    }
                }
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteAuthData(String authToken) throws DataAccessException {
        var statement = "DELETE FROM authdata WHERE authtoken = ?";
        try (var conn = getConnection()){
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void insertAuthData(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO authdata (username, authtoken) VALUES (?, ?)";
        try (var conn = getConnection()){
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1,authData.username());
                preparedStatement.setString(2,authData.authToken());
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        // call getAuthData
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE TABLE authdata";
        try (var conn = getConnection()){
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }

    }
}
