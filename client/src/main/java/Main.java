import chess.*;
import repls.PreLoginREPL;
import facade.ServerFacade;

public class Main {
    public static void main(String[] args) {
        ServerFacade facade = new ServerFacade();
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        PreLoginREPL preLogin = new PreLoginREPL(facade);
        preLogin.run();
    }
}