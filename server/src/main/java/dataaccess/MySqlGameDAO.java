package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import javax.xml.crypto.Data;
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
            var conn = getConnection();
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
            var conn = getConnection();
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        }   catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    static Connection getConnection() throws DataAccessException {
        var conn = DatabaseManager.getConnection();
        return conn;
    }

    /**
     * TODO: Make get games return the table
     *
     */
    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        var games = new ArrayList<GameData>();
        var statement = "SELECT * FROM games";
        try (var conn = getConnection()){
            try (var preparedStatement = conn.prepareStatement(statement)) {
                var result = preparedStatement.executeQuery();
                while (result.next()) {
                    var gameID = result.getInt("gameID");
                    var whiteUsername = result.getString("whiteUsername");
                    var blackUsername = result.getString("blackUsername");
                    var chessName = result.getString("chessName");
                    var chessGame = gameDeserializer(result.getString("chessGame"));
                    games.add(new GameData(gameID, chessName, chessGame, whiteUsername, blackUsername));
                }
            }
        }catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
        return games; // mysql get the table
    }

    @Override
    public GameData getGame(int id) throws DataAccessException { // "SELECT whiteUsername, blackUsername, gameName, chessGame FROM game WHERE gameID=?"
        var statement = "SELECT * FROM games WHERE gameID=?";
        try (var conn = getConnection()){
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1, id);
                try (var result = preparedStatement.executeQuery()){
                    if (result.next()) {
                        var gameID = result.getInt("gameID");
                        var whiteUsername = result.getString("whiteUsername");
                        var blackUsername = result.getString("blackUsername");
                        var gameName = result.getString("chessName");
                        var chessGame = gameDeserializer(result.getString("chessGame"));
                        return new GameData(id, gameName, chessGame, whiteUsername, blackUsername);
                    }
                }
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
       throw new DataAccessException("unable to find game");
    }

    @Override
    public void createGame(GameData game) throws DataAccessException { // "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, chessGame) VALUES(?, ?, ?, ?, ?)"
        var statement = "INSERT INTO games (gameID, whiteUsername, blackUsername, chessName, chessGame) VALUES(?, ?, ?, ?, ?)";
        try (var conn = getConnection()){
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1,game.gameID());
                preparedStatement.setString(2,game.whiteUsername());
                preparedStatement.setString(3,game.blackUsername());
                preparedStatement.setString(4,game.gameName());
                preparedStatement.setString(5, gameSerializer(game.game()));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException { // "UPDATE game SET whiteUsername=?, blackUsername=?, gameName=?, chessGame=? WHERE gameID=?"
        var statement = "UPDATE games SET whiteUsername=?, blackUsername=?, chessName=?, chessGame=? WHERE gameID=?";
        try (var conn = getConnection()){
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, game.whiteUsername());
                preparedStatement.setString(2, game.blackUsername());
                preparedStatement.setString(3, game.gameName());
                preparedStatement.setString(4, gameSerializer(game.game()));
                preparedStatement.setInt(5, game.gameID());
                preparedStatement.executeUpdate(); // maybe add a check
            }
        }catch (SQLException | DataAccessException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException { //"TRUNCATE game"
        var statement = "TRUNCATE TABLE games";
        try (var conn = getConnection()){
            try (var preparedStatement = getConnection().prepareStatement(statement)){
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public boolean getGameByname(String name) throws DataAccessException {
        var statement = "SELECT * FROM games WHERE chessName=?";
        try (var conn = getConnection()){
            try (var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setString(1, name);
                try (var result = preparedStatement.executeQuery()){
                    if (result.next()){
                        return true;
                    }
                }
            }
        }catch (SQLException e){
            return false;
        }
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
