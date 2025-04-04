package model;

import chess.ChessGame;

import java.util.ArrayList;

public record GameData(int gameID, String gameName,  ChessGame game, String whiteUsername, String blackUsername, ArrayList<String> observers) {
    public GameData(int gameID, String gameName,  ChessGame game, String whiteUsername, String blackUsername){
        this(gameID, gameName, game, whiteUsername, blackUsername, null);
    }

    public GameData(String gameName){
        this(0, gameName, null, null, null,null);

    }

    public GameData(int gameID, String gameName, ChessGame game) {
        this(gameID, gameName, game, null, null,null);
    }

    public GameData( String whiteUsername, int gameID){
        this(gameID, null, null, whiteUsername, null,null);
    }






}
