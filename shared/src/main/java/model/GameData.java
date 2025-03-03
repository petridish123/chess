package model;

import chess.ChessGame;

public record GameData(int gameID, String gameName,  ChessGame game, String whiteUsername, String blackUsername) {


    public GameData(String gameName){
        this(0, gameName, null, null, null);
//        this.gameName = gameName;
//        this.game = null;
//        this.whiteUsername = "";
//        this.blackUsername = "";
//        this.gameID = 0;
    }

    public GameData(int gameID, String gameName, ChessGame game) {
        this(gameID, gameName, game, null, null);
//        this.gameID = gameID;
//        this.gameName = gameName;
//        this.game = game;
//        this.whiteUsername = "";
//        this.blackUsername = "";
    }

    public GameData(int gameID, String whiteUsername){
        this(gameID, null, null, whiteUsername, null);
    }






}
