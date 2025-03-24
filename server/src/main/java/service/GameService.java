package service;

import model.*;
import dataaccess.*;

import java.util.ArrayList;
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
        try {
            gameDAO.clear();
            authTokenDAO.clear();
        } catch (DataAccessException e) {
            return;
        }
    }

    public ArrayList<GameData> listGames(String authToken) throws DataAccessException {

        authTokenDAO.getAuthData(authToken); // throws data access exception
        return gameDAO.listGames();
    }

    public int createGame(String authToken, String gameName) throws DataAccessException {
        authTokenDAO.getAuthData(authToken);

        if (gameDAO.getGameByname(gameName)){
            throw new DataAccessException("Game already exists");
        } // if the game exists, return
        int newGameID = this.listGames(authToken).size()+1;
        ChessGame newChessGame = new ChessGame(); // sets up a new board
        GameData newGame = new GameData(newGameID, gameName, newChessGame);
        gameDAO.createGame(newGame); // When implementing the SQL version, make sure to throw the exception in the DAO
        return newGameID;
    }

    public void joinGame(String authToken, int gameID, String color) throws DataAccessException {

        GameData game = gameDAO.getGame(gameID); // throws exception if it doesn't exist

        AuthData authData = authTokenDAO.getAuthData(authToken); // throws error as well *kissy face*
        String username = authData.username();

        String whiteUser = game.whiteUsername();
        String blackUser = game.blackUsername();
        ChessGame chessGame = game.game();
        String gameName = game.gameName();

        if (Objects.equals(color, "WHITE")){
            if (Objects.equals(game.whiteUsername(), null)||game.whiteUsername().isEmpty()){
                whiteUser = username;
            }
            else{
                throw new UnAuthorizedException("White username already exists");
            }
        }
        else if (Objects.equals(color, "BLACK")){
            if (Objects.equals(game.blackUsername(), null)||game.blackUsername().isEmpty()){
                blackUser = username;
            }
            else{
                throw new UnAuthorizedException("Black username already exists");
            }
        } else{
            throw new InvalidInput("Not a valid color");
        }
        GameData newGame = new GameData(gameID, gameName,game.game(), whiteUser, blackUser );
        gameDAO.updateGame(newGame);
    }


}
