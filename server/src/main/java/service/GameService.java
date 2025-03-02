package service;

import model.*;
import dataaccess.*;
import java.util.HashSet;
/*
 *  clear       ✅
 *  add game
 *  list games  ✅
 *  join game
 *
 */



public class GameService {

    GameDataAccess gameDAO;
    AuthTokenDataAccess authTokenDAO;
    public GameService(GameDataAccess gameDAO, AuthTokenDataAccess authTokenDAO  ) {
        this.gameDAO = gameDAO;
        this.authTokenDAO = authTokenDAO;
    }

    void clear() {
        gameDAO.clear();
        authTokenDAO.clear();
    }

    HashSet<GameData> listGames(AuthData authData) throws DataAccessException {
        authTokenDAO.getAuthData(authData.authToken()); // throws data access exception
        return gameDAO.listGames();
    }

    GameData createGame(AuthData authData, String gameName) throws DataAccessException {
        authTokenDAO.getAuthData(authData.authToken()); // throws data access exception
        gameDAO.createGame(new GameData()); // When implementing the SQL version, make sure to throw the exception in the DAO
    }


}
