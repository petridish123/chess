package repls;

import chess.ChessGame;
import model.GameData;
import server.ServerFacade;

import java.util.Objects;
import java.util.Scanner;
import model.GameData;
import ui.BoardPrinter;

import static ui.EscapeSequences.*;

import java.util.ArrayList;


import static java.lang.System.out;

import static java.lang.System.out;

public class gameREPL {
    ServerFacade facade;
    postLoginREPL postREPL;
    int gameID;
    String color;
    GameData game;
    public gameREPL(ServerFacade facade, postLoginREPL postREPL) {
        this.facade = facade;
        this.postREPL = postREPL;
    }

    void setGame(int gameID, String color, GameData game) {
        this.gameID = gameID;
        this.color = color;
        this.game = game;
    }

    void run(){
        out.print(RESET_TEXT_COLOR + RESET_BG_COLOR);
        var playing = true;
        while(playing){
            //print board here
            String[] input = getUserInput();
            switch(input[0]){
                case "board":
                    ChessGame.TeamColor teamColor = (Objects.equals(color, "WHITE")) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
                    new BoardPrinter(game.game()).printBoard(teamColor);
                    break;
                case "quit":
                    return;
                case "leave": // actually leave the game here!
                    postREPL.run();
                    return;
                default:
                    out.println("help: this menu \nquit: leave the game \nboard: print the board \nleave : leave the game");
                    out.println("on your turn, type the starting position followed by a valid end position");
                    break;
            }
        }
    }

    private String[] getUserInput() {
        out.print("\n[" + gameID + "] >>> ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().split(" ");
    }

}
