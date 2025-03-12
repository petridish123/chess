package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;

/**
 * This class will implement mySQL into each of the DAO functions :)
 */
public class MySqlGameDAO implements GameDataAccess{
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


    public MySqlGameDAO() throws DataAccessException {
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
                        CREATE TABLE IF NOT EXISTS games (
                            gameID INT NOT NULL AUTO_INCREMENT,
                            whiteUsername VARCHAR(255),
                            blackUsername VARCHAR(255),
                            chessName VARCHAR(255),
                            chessGame TEXT,
                            PRIMARY KEY (gameID)
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

    /**
     * TODO: Make get games return the table
     *
     */
    @Override
    public ArrayList<GameData> listGames() {
        return null; // mysql get the table
    }

    @Override
    public GameData getGame(int id) throws DataAccessException { // "SELECT whiteUsername, blackUsername, gameName, chessGame FROM game WHERE gameID=?"
        var statement = "SELECT * FROM games WHERE gameID=?";
        try (var conn = getConnection()){
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1, id);
                try (var result = preparedStatement.executeQuery()){
                    var whiteUsername = result.getString(1);
                    var blackUsername = result.getString(2);
                    var gameName = result.getString(3);
                    var chessGame = gameDeserializer(result.getString(4));
                    return new GameData(id, gameName,chessGame, whiteUsername, blackUsername);
                }
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void createGame(GameData game) throws DataAccessException { // "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, chessGame) VALUES(?, ?, ?, ?, ?)"
        var statement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, chessGame) VALUES(?, ?, ?, ?, ?)";
        try (var conn = getConnection()){
            try (var preparedStatement = conn.prepareStatement(statement)) {
                
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void updateGame(GameData game) { // "UPDATE game SET whiteUsername=?, blackUsername=?, gameName=?, chessGame=? WHERE gameID=?"

    }

    @Override
    public void clear() { //"TRUNCATE game"

    }

    @Override
    public boolean getGameByname(String name) throws DataAccessException {
        return false;
    }

    // maybe make serializer and deserializer for the game object specifically so I can turn it to a string and then back to an object using GSON
    String gameSerializer(ChessGame game) {
        return new Gson().toJson(game);
    }

    ChessGame gameDeserializer(String game) {
        return new Gson().fromJson(game, ChessGame.class);
    }
}
