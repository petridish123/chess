package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

public class MySqlUserDao implements UserDataAccess {
    private final String SALT = BCrypt.gensalt(12);


    @Override
    public UserData getUserData(String username) throws DataAccessException {
        var statement = "SELECT * FROM users WHERE username = ?";
        try (var conn = DatabaseManager.getConnection();){
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String email = resultSet.getString("email");
                        String password = (resultSet.getString("password"));
                        return new UserData(username, password, email);
                    }
                }
            }
        } catch (SQLException e){
            throw new DataAccessException("User does not exist");
        }
        throw new DataAccessException("User does not exist");
        //return null;
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection();){
            try (var preparedStatement = conn.prepareStatement(statement)) {
                var password = "";
                if (Objects.equals(user.password(), null)){
                    password = "";
                }
                else{
                    password = BCrypt.hashpw(user.password(),this.SALT);
                }

                preparedStatement.setString(1, user.username());
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, user.email());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException("User already exists");
        }
    }

    @Override
    public boolean authenticate(String username, String password) throws DataAccessException {
        return BCrypt.checkpw(password, this.getUserData(username).password());
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE TABLE users";
        try (var conn = DatabaseManager.getConnection();){
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }

        }catch (SQLException e){
            throw new DataAccessException("Not clear");
        }

    }
}
