package service;

import dataaccess.*;
import model.*;
import java.util.UUID;

/*
 * User Service endpoints
 *
 * register ✅
 * login
 * logout
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

    

}