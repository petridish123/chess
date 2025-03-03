package model;

import chess.ChessGame;

public record GameData(int gameID, String gameName,  ChessGame game, String whiteUsername, String blackUsername) {

    public GameData(String gameName){
        this(0, gameName, null, null, null);

    }

    public GameData(int gameID, String gameName, ChessGame game) {
        this(gameID, gameName, game, null, null);
    }

    public GameData( String whiteUsername, int gameID){
        this(gameID, null, null, whiteUsername, null);
    }






}
