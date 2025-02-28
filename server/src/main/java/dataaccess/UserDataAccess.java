package dataaccess;

import model.*;

public interface UserDataAccess {

    UserData getUserData(String username) throws DataAccessException;

    void createUser(UserData user) throws DataAccessException;

    boolean authenticate(String username, String password) throws DataAccessException;

    void clear();

}
