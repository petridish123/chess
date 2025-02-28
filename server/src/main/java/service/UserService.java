package service;

import dataaccess.*;

public class UserService {

    UserDataAccess userDAO;
    AuthTokenDataAccess authTokenDAO;

    public UserService(UserDataAccess userDAO, AuthTokenDataAccess authTokenDAO) {
        this.userDAO = userDAO;
        this.authTokenDAO = authTokenDAO;
    }

    
}