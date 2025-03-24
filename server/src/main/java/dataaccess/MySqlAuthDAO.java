package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySqlAuthDAO implements AuthTokenDataAccess{



    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException {
        var statement = "SELECT * FROM authdata WHERE authtoken = ?";
        try (var conn = DatabaseManager.getConnection();){
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
            throw new BadAuth("unable to find authtoken");
        }
        throw new BadAuth("unable to find authtoken");
    }

    @Override
    public void deleteAuthData(String authToken) throws DataAccessException {
        var statement = "DELETE FROM authdata WHERE authtoken = ?";
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException |DataAccessException e){
            throw new DataAccessException("unable to delete authtoken");
        }


    }

    @Override
    public void insertAuthData(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO authdata (username, authtoken) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1,authData.username());
                preparedStatement.setString(2,authData.authToken());
                preparedStatement.executeUpdate();
            }
        }catch (SQLException | DataAccessException e){
            throw new DataAccessException("GYAH");
        }
        // call getAuthData
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE TABLE authdata";
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }

    }
}
