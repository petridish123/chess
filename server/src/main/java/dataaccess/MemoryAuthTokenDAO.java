package dataaccess;
import dataaccess.DataAccessException;
import model.AuthData;
import java.util.HashSet;

public class MemoryAuthTokenDAO implements AuthTokenDataAccess{

    public HashSet<AuthData> database;

    public MemoryAuthTokenDAO(){
        database = new HashSet<AuthData>();
    }


    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException {
        for(AuthData authData : database){
            if (authData.authToken().equals(authToken)){
                return authData;
            }
        }
        throw new DataAccessException("AuthData does not exist");
    }

    @Override
    public void deleteAuthData(String authToken) {
        database.removeIf(authData -> authData.authToken().equals(authToken));
    }

    @Override
    public void insertAuthData(AuthData authData) throws  DataAccessException{
        try {
            this.getAuthData(authData.authToken());
            throw new DataAccessException("Already taken");
        }
        catch (DataAccessException e) {
            database.add(authData);
        }

    }

    @Override
    public void clear(){
        database.clear();
    }
}
