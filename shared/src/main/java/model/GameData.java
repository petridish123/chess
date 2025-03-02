package model;

import chess.ChessGame;

public record GameData(int gameID, String gameName,String whiteUsername, String blackUsername,  ChessGame game) {

    public GameData(int gameID, String gameName, ChessGame game) {
        this.gameID = gameID;
        this.gameName = gameName;
        this.game = game;

        this.whiteUsername = "";
        this.blackUsername = "";
    }

    public GameData(int gameID, String gameName,String whiteUsername, String blackUsername,  ChessGame game){
        this.gameID = gameID;
        this.gameName = gameName;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.game = game;
    }
}
