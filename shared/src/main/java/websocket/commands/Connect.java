package websocket.commands;

import chess.ChessGame;

public class Connect extends UserGameCommand{
    int gameID;
    String username;
    ChessGame.TeamColor teamColor;
    public Connect(String authToken, Integer gameID, ChessGame.TeamColor teamColor, String username) {
        super(CommandType.CONNECT, authToken, gameID);
        this.gameID = gameID;
        this.teamColor = teamColor;
        this.username = username;
    }

    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }
    public String getUsername() {
        return username;
    }

}
