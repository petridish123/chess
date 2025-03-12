package dataaccess;

import model.*;

public interface UserDataAccess {

    UserData getUserData(String username) throws DataAccessException;

    void createUser(UserData user) throws DataAccessException;



    void clear() throws DataAccessException;

}
