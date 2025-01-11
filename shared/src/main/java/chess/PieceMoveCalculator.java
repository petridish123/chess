package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMoveCalculator {

    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessPiece piece;

    public PieceMoveCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
        this.piece = board.getPiece(position);
    }
    public boolean inBounds(ChessPosition pos){
        return pos.getColumn() >= 0 && pos.getColumn() < 8 && pos.getRow() >= 0 && pos.getRow() < 8;
    }
    public Collection<ChessMove> KingMovesCalculator(){
        var PossibleMoves = new ArrayList<ChessMove>();

        for (var row = -1; row < 2; row++) {
            for (var col = -1; col < 2; col++) {
                ChessPosition pos = new ChessPosition(position.getRow() + row, position.getColumn()+col);
                if (inBounds(pos) && board.getPiece(pos) == null) {
                    ChessMove move = new ChessMove(position, pos, null );
                    PossibleMoves.add(move);
                    System.out.println(move.toString());
                }
            }
        }
        return PossibleMoves;
    }


}
