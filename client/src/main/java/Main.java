import chess.*;
import repls.preLoginREPL;
import Facade.ServerFacade;

public class Main {
    public static void main(String[] args) {
        ServerFacade facade = new ServerFacade();
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        preLoginREPL preLogin = new preLoginREPL(facade);
        preLogin.run();
    }
}