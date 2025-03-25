package repls;

import chess.ChessGame;
import model.GameData;
import facade.ServerFacade;

import java.util.Objects;
import java.util.Scanner;

import ui.BoardPrinter;

import static ui.EscapeSequences.*;


import static java.lang.System.out;

public class GameREPL {
    ServerFacade facade;
    PostLoginREPL postREPL;
    int gameID;
    String color;
    GameData game;
    boolean observer = false;
    public GameREPL(ServerFacade facade, PostLoginREPL postREPL) {
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
        if (Objects.isNull(color)) {
            color = "WHITE";
            observer = true;
        }
        while(playing){
            //print board here
            String[] input = getUserInput();
            switch(input[0]){
                case "redraw":
                    ChessGame.TeamColor teamColor = (Objects.equals(color, "WHITE")) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
                    new BoardPrinter(game.game()).printBoard(teamColor);
                    break;
                case "quit":
                    return;
                case "leave":
                    if (observer){
                        postREPL.run();
                        return;    // actually leave the game here!
                    }
                    postREPL.run();
                    return;
                case "move":
                    if (observer){
                        out.println("You are only observing this game, you cannot make moves");
                        break;
                    }
                case "resign":
                    postREPL.run();
                    return;
                case "highlight":
                    break;
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

    private void helpPrint(){
        out.println("help : this menu");
        out.println("leave : leave the game");
    }

}
