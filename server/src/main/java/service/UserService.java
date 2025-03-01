package service;

import dataaccess.*;
import model.*;
import java.util.UUID;

/*
 * User Service endpoints
 *
 * register ✅
 * login    ✅
 * logout   ✅
 *
 * */

public class UserService {

    UserDataAccess userDAO;
    AuthTokenDataAccess authTokenDAO;

    public UserService(UserDataAccess userDAO, AuthTokenDataAccess authTokenDAO) {
        this.userDAO = userDAO;
        this.authTokenDAO = authTokenDAO;
    }

    AuthData registerUser(UserData user) throws DataAccessException {

        userDAO.createUser(user); // can throw a data access exception when the user is already in the DB

        var authToken = UUID.randomUUID().toString();
        var auth = new AuthData(authToken, user.username());
        authTokenDAO.insertAuthData(auth); // can also throw an exception

        return auth;
    }

    AuthData loginUser(UserData user) throws DataAccessException { // receives only username and password
        /**
         * @user takes in only password and username to check the DB
         */
        UserData potentialUser = userDAO.getUserData(user.username()); // throws DataAccessException "no user"
        if (!potentialUser.password().equals(user.password())) { // Finds the user based on username and then checks password
            throw new DataAccessException("Wrong password");
        }
        var authToken = UUID.randomUUID().toString();
        var auth = new AuthData(authToken, user.username());
        authTokenDAO.insertAuthData(auth); // throws exception "already taken"
        return auth;
    }

    void logoutUser(AuthData authToken) throws DataAccessException {
        var token = authTokenDAO.getAuthData(authToken.authToken()); // does not exist
        authTokenDAO.deleteAuthData(token.authToken());
    }
    

}