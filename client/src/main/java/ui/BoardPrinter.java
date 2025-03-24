package ui;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

import static java.lang.System.out;
import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLACK;

public class BoardPrinter {
    String startB = " h"+ EMPTY  +"g"+ EMPTY  +"f"+ EMPTY  +"e"+ EMPTY  +"d"+ EMPTY  +"c"+ EMPTY  +"b" + EMPTY+ "a ";
    String startW = " a"+ EMPTY  +"b"+ EMPTY  +"c"+ EMPTY  +"d"+ EMPTY  +"e"+ EMPTY  +"f"+ EMPTY  +"g" + EMPTY+ "h ";
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
                (!reversed ? "    a  b  c  d  e  f  g  h    " : "    h  g  f  e  d  c  b  a    ") +
                RESET_BG_COLOR;

    }

    private String boardRow(int row, boolean reversed) {
        String output = "";
        output += SET_BG_COLOR_BLACK;
        output += (" %d ".formatted(row));

        for (int i = 1; i < 9; i++) {
            int column = !reversed ? i : (i * -1) + 9;
            output += SET_BG_COLOR_BLACK; // set the right one
            output += (piece(row,column));
        }


        output+=(SET_BG_COLOR_BLACK);
        output+=(" %d ".formatted(row));
        output+=(RESET_BG_COLOR);
        output+=(RESET_TEXT_COLOR);


        return output;
    }

    private String correctBackgroundColor(int row, int column) {
        String output = "";
        return output + SET_BG_COLOR_BLACK;
    }

    private String piece(int row, int column) {
        String output = "";
        ChessPosition position = new ChessPosition(row, column);
        ChessPiece piece = game.getBoard().getPiece(position);

        if (piece != null) {

            switch (piece.getPieceType()) {
                case QUEEN -> output += (piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_QUEEN : BLACK_QUEEN);
                case KING -> output += (piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KING : BLACK_KING );
                case BISHOP -> output += (piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_BISHOP : BLACK_BISHOP);
                case KNIGHT -> output += (piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KNIGHT : BLACK_KNIGHT);
                case ROOK -> output += (piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_ROOK : BLACK_ROOK);
                case PAWN -> output += (piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_PAWN : BLACK_PAWN);
            }
        } else {
            output += EMPTY;
        }

        return output;
    }
}