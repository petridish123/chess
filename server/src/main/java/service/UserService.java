package service;

import dataaccess.*;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

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



    public AuthData registerUser(UserData user) throws DataAccessException {

        userDAO.createUser(user); // can throw a data access exception when the user is already in the DB

        var authToken = UUID.randomUUID().toString();
        var auth = new AuthData(authToken, user.username());

        authTokenDAO.insertAuthData(auth); // can also throw an exception

        return auth;
    }

    public AuthData loginUser(UserData user) throws DataAccessException {
        /**
         * @user takes in only password and username to check the DB
         */
        UserData potentialUser = userDAO.getUserData(user.username());// throws DataAccessException "no user"

        if (!userDAO.authenticate(potentialUser.username(),user.password())) {
            throw new DataAccessException("Wrong password");
        }
        var authToken = UUID.randomUUID().toString();
        var auth = new AuthData(authToken, user.username());
        boolean taken = false;
        do {
            try {
                taken = false;
                authTokenDAO.insertAuthData(auth); // throws exception "already taken"
            }catch (DataAccessException dae) {
                taken = true;
            }


        }while (taken);

        return auth;
    }

    public void logoutUser(String authToken) throws DataAccessException {
        var token = authTokenDAO.getAuthData(authToken); // does not exist
        authTokenDAO.deleteAuthData(token.authToken());
    }

    public void clear(){
        try {
            authTokenDAO.clear();
            userDAO.clear();
        } catch (DataAccessException e) {
            return;
        }
    }
    

}