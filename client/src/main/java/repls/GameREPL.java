package repls;

import chess.ChessGame;
import chess.ChessPiece;
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
    public static String color;
    GameData game;
    boolean observer = false;
    public static BoardPrinter boardPrinter;
    public GameREPL(ServerFacade facade, PostLoginREPL postREPL) {
        this.facade = facade;
        this.postREPL = postREPL;
    }

    void setGame(int gameID, String color, GameData game) {
        this.gameID = gameID;
        this.color = color;
        this.game = game;
        boardPrinter = new BoardPrinter(game.game());
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
                case "redraw": // ws load board
                    ChessGame.TeamColor teamColor = (Objects.equals(color, "WHITE")) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
                    boardPrinter.printBoard(teamColor);
                    break;

                case "leave":
                    if (observer){
                        postREPL.run();
                        return;    // actually leave the game here!
                    }
                    postREPL.run();
                    return;
                case "move": // send to server facade but it only makes a ws call
                    if (observer){
                        out.println("You are only observing this game, you cannot make moves");
                        break;
                    }
                    else{
                        ChessPiece.PieceType promotion = null;
                        if (input.length < 3){
                            out.println("Not enough arguments");
                            out.println("move <start position letter> <end position number>");
                        }
                        if (input.length >= 4){
                            promotion = getPieceType(input[3]);
                        }
                        if (!facade.makeMove(gameID,input[1], input[2],promotion)){
                            out.println("Move failed!");

                        }
                        else {
                            out.println("Move successful!");
                        }
                    }
                    break;
                case "resign":
                    postREPL.run();
                    return;
                case "highlight":
                    break;
                default:
                    helpPrint();
                    break;
            }
        }
    }

    private String[] getUserInput() {
        out.print("\n[IN-GAME] >>> ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().split(" ");
    }

    private void helpPrint(){
        out.println("help : this menu");
        out.println("leave : leave the game");
        out.println("redraw : print the board");
        out.println("highlight <position> : highlight the valid moves of a certain piece");
        out.println("resign : resign the game");
        out.println("move <start position> <end position> : move piece from start position to end position");

    }

    private void move(){

    }
    private void resign(){

    }
    private void printBoard(ChessGame.TeamColor teamColor){

    }
    private void highlight(){

    }
    private void leave(){

    }

    public ChessPiece.PieceType getPieceType(String name) {
        return switch (name.toUpperCase()) {
            case "QUEEN" -> ChessPiece.PieceType.QUEEN;
            case "BISHOP" -> ChessPiece.PieceType.BISHOP;
            case "KNIGHT" -> ChessPiece.PieceType.KNIGHT;
            case "ROOK" -> ChessPiece.PieceType.ROOK;
            case "PAWN" -> ChessPiece.PieceType.PAWN;
            default -> null;
        };
    }

}
