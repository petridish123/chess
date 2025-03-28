package ui;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

import static java.lang.System.out;
import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLACK;

public class BoardPrinter {
    String startB = EMPTY + "h   g   f  e   d  c   b   a ";
    String startW = EMPTY + "a   b   c  d   e   f  g   h ";
    ChessGame game;

    public BoardPrinter(ChessGame game) {
        this.game = game;
    }

    public void updateGame(ChessGame game) {
        this.game = game;
    }


    public void printBoard(ChessGame.TeamColor color) {
        boolean reversed = color == ChessGame.TeamColor.BLACK;
        out.println(startingRow(reversed));
        for (int i = 8; i > 0 ; i--) {
            int row = !reversed ? i : (i * -1) + 9;
            out.println(boardRow(row,reversed));
        }
        out.println(startingRow(reversed));
    }

    private String startingRow(boolean reversed) {
        return SET_BG_COLOR_DARK_GREY+
                (!reversed ? startW : startB) +
                RESET_BG_COLOR;

    }

    private String boardRow(int row, boolean reversed) {
        String output = "";
        output += SET_BG_COLOR_BLACK;
        output += (" %d ".formatted(row));

        for (int i = 1; i < 9; i++) {
            int column = !reversed ? i : (i * -1) + 9;
//            row = !reversed ? i : (i * -1) + 9;
            output += correctBackgroundColor(row, column); // set the right one
            output += (piece(row,column));
        }


        output+=(SET_BG_COLOR_BLACK);
        output+=(" %d ".formatted(row));
        output+=(RESET_BG_COLOR);
        output+=(RESET_TEXT_COLOR);


        return output;
    }

    private String correctBackgroundColor(int row, int column) {
        return (((row+column) % 2) == 0) ? SET_BG_COLOR_BLACK : SET_BG_COLOR_WHITE;
    }

    private String piece(int row, int column) {
        String output = "";
        ChessPosition position = new ChessPosition(row, column);
        ChessPiece piece = game.getBoard().getPiece(position);

        if (piece != null) {
            output += SET_TEXT_COLOR_RED;
            switch (piece.getPieceType()) {
                case QUEEN -> output += (piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_QUEEN : BLACK_QUEEN);
                case KING -> output += (piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KING : BLACK_KING );
                case BISHOP -> output += (piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_BISHOP : BLACK_BISHOP);
                case KNIGHT -> output += (piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KNIGHT : BLACK_KNIGHT);
                case ROOK -> output += (piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_ROOK : BLACK_ROOK);
                case PAWN -> output += (piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_PAWN : BLACK_PAWN);
            }
            output += RESET_TEXT_COLOR;
        } else {
            output += EMPTY;
        }

        return output;
    }
}