package dataaccess;

import model.GameData;
import model.UserData;

import java.util.HashSet;
import model.*;
public class MemoryUserDAO implements UserDataAccess{
    public HashSet<UserData> database;

    public MemoryUserDAO() {
        database = new HashSet<UserData>();
    }

    @Override
    public UserData getUserData(String username) throws DataAccessException {
        for (UserData userData : database) {
            if (userData.username().equals(username)) {
                return userData;
            }
        }
        throw new DataAccessException("User does not exist");
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        try{
            getUserData(user.username());
        }
        catch(DataAccessException e){
            database.add(user);
            return;
        }
        throw new DataAccessException("User already exists");

    }

    @Override
    public boolean authenticate(String username, String password) throws DataAccessException {
        getUserData(username);
        return password.equals(getUserData(username).password());
    }

    @Override
    public void clear() {
        database.clear();
    }
}
