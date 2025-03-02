package service;

import model.*;
import dataaccess.*;
import java.util.HashSet;
import chess.*;
/*
 *  clear       ✅
 *  add game    ✅
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

    HashSet<GameData> listGames(String authToken) throws DataAccessException {
        authTokenDAO.getAuthData(authToken); // throws data access exception
        return gameDAO.listGames();
    }

    int createGame(String authToken, String gameName) throws DataAccessException {
        authTokenDAO.getAuthData(authToken); // throws data access exception
        if (gameDAO.getGameByname(gameName)){
            throw new DataAccessException("Game already exists");
        } // if the game exists, return
        int new_gameID = this.listGames(authToken).size()+1;
        ChessGame new_chess_game = new ChessGame(); // sets up a new board
        GameData new_game = new GameData(new_gameID, gameName, new_chess_game);
        gameDAO.createGame(new_game); // When implementing the SQL version, make sure to throw the exception in the DAO
        return new_gameID;
    }

    void joinGame(String authToken, int gameID, String Color) throws DataAccessException {
        gameDAO.getGame(gameID); // throws exception if it doesn't exist
        // check to see if the game exists (getGame)
        // get authToken and see if it is correct
        // get the username from the authData

    }


}
