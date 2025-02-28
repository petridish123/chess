package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, int whiteScore, int blackScore, String gameName, ChessGame game) {
}
