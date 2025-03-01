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




}
