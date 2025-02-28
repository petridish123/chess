package dataaccess;
import dataaccess.DataAccessException;
import model.*;
public interface AuthTokenDataAccess {

    public AuthData getAuthData(String authToken) throws dataaccess.DataAccessException;
    public void deleteAuthData(String authToken);
    public void insertAuthData(AuthData authData) throws  DataAccessException;
    public void clear();
}
