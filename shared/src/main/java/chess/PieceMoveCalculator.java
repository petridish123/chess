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

    public Collection<ChessMove> KingMovesCalculator(){
        return new ArrayList<ChessMove>();
    }


}
