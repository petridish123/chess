package service;

import model.*;
import dataaccess.*;
import java.util.HashSet;
import java.util.Objects;

import chess.*;
/*
 *  clear       ✅
 *  add game    ✅
 *  list games  ✅
 *  join game   ✅
 *
 */



public class GameService {

    GameDataAccess gameDAO;
    AuthTokenDataAccess authTokenDAO;
    public GameService(GameDataAccess gameDAO, AuthTokenDataAccess authTokenDAO  ) {
        this.gameDAO = gameDAO;
        this.authTokenDAO = authTokenDAO;
    }

    public void clear() {
        gameDAO.clear();
        authTokenDAO.clear();
    }

    public HashSet<GameData> listGames(String authToken) throws DataAccessException {
        authTokenDAO.getAuthData(authToken); // throws data access exception
        return gameDAO.listGames();
    }

    public int createGame(String authToken, String gameName) throws DataAccessException {
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

    public void joinGame(String authToken, int gameID, String Color) throws DataAccessException {
        GameData game = gameDAO.getGame(gameID); // throws exception if it doesn't exist
        AuthData authData = authTokenDAO.getAuthData(authToken); // throws error as well *kissy face*
        String username = authData.username();

        String whiteUser = game.whiteUsername();
        String blackUser = game.blackUsername();
        ChessGame chessGame = game.game();
        String gameName = game.gameName();

        if (Objects.equals(Color, "WHITE")){
            if (game.whiteUsername().isEmpty()){
                whiteUser = username;
            }
            else{
                throw new DataAccessException("White username already exists");
            }
        }
        else if (Objects.equals(Color, "BLACK")){
            if (game.blackUsername().isEmpty()){
                blackUser = username;
            }
            else{
                throw new DataAccessException("Black username already exists");
            }
        }
        GameData new_game = new GameData(gameID, gameName,game.game(), whiteUser, blackUser );
        gameDAO.updateGame(new_game);
    }


}
