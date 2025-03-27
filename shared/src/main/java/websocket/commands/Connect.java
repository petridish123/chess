package websocket.commands;

import chess.ChessGame;

public class Connect extends UserGameCommand{
    int gameID;
    ChessGame.TeamColor teamColor;
    public Connect(String authToken, Integer gameID, ChessGame.TeamColor teamColor) {
        super(CommandType.CONNECT, authToken, gameID);
        this.gameID = gameID;
        this.teamColor = teamColor;
    }

    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

}
