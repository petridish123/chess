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

    public ArrayList<ChessMove> calculate() {
        ChessPiece.PieceType type = piece.getPieceType();

        if (type == ChessPiece.PieceType.KING) {
            return KingMovesCalculator();
        }
        if (type == ChessPiece.PieceType.QUEEN) {
            return QueenMovesCalculator();
        }
        if (type == ChessPiece.PieceType.BISHOP) {
            return BishopMovesCalculator();
        }
        if (type == ChessPiece.PieceType.KNIGHT) {
            return KnightMoves();
        }
        if (type == ChessPiece.PieceType.PAWN) {
            if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                return PawnMoves(ChessGame.TeamColor.BLACK);
            }

            return PawnMoves(ChessGame.TeamColor.WHITE);
        }
        if (type == ChessPiece.PieceType.ROOK) {
            return RookMovesCalculator();
        }
        return new ArrayList<ChessMove>();
    }


    public boolean inBounds(ChessPosition pos){
        return pos.getColumn() > 0 && pos.getColumn() <= 8 && pos.getRow() > 0 && pos.getRow() <= 8;
    }
    public boolean OtherColor(ChessPosition pos){
        return board.getPiece(pos).getTeamColor() != piece.getTeamColor();
    }


    public ArrayList<ChessMove> KingMovesCalculator(){
        var PossibleMoves = new ArrayList<ChessMove>();
        for (var row = -1; row < 2; row++) {
            for (var col = -1; col < 2; col++) {
                ChessPosition pos = new ChessPosition(position.getRow() + row, position.getColumn()+col);
                if (inBounds(pos) && (board.getPiece(pos) == null || OtherColor(pos))) {
                    ChessMove move = new ChessMove(position, pos, null );
                    PossibleMoves.add(move);
                }
            }
        }
        return PossibleMoves;
    }

    public ArrayList<ChessMove> RookMovesCalculator(){
        var PossibleMoves = new ArrayList<ChessMove>();
        ChessPosition pos = new ChessPosition(position.getRow(), position.getColumn());
        // To the left now y'all
        for (var row = -1; position.getRow() + row > 0; row--) {
            pos = new ChessPosition(position.getRow() + row, position.getColumn());
            if (board.getPiece(pos) == null || OtherColor(pos)) {
                ChessMove move = new ChessMove(position, pos, null );
                PossibleMoves.add(move);
            }
            if (board.getPiece(pos) != null) break;
        }
        for (var row = +1; position.getRow() + row <= 8; row++) {
            pos = new ChessPosition(position.getRow() + row, position.getColumn());
            if (board.getPiece(pos) == null || OtherColor(pos)) {
                ChessMove move = new ChessMove(position, pos, null );
                PossibleMoves.add(move);
            }
            if (board.getPiece(pos) != null) break;
        }
        for (var col = -1; position.getColumn() + col > 0; col--) {
            pos = new ChessPosition( position.getRow(), position.getColumn() + col);
            if (board.getPiece(pos) == null || OtherColor(pos)) {
                ChessMove move = new ChessMove(position, pos, null );
                PossibleMoves.add(move);
            }
            if (board.getPiece(pos) != null) break;
        }
        for (var col = 1; position.getColumn() + col <= 8; col++) {
            pos = new ChessPosition(position.getRow(), position.getColumn() + col);
            if (board.getPiece(pos) == null || OtherColor(pos)) {
                ChessMove move = new ChessMove(position, pos, null );
                PossibleMoves.add(move);
            }
            if (board.getPiece(pos) != null) break;
        }

        return PossibleMoves;
    }

    public ArrayList<ChessMove> BishopMovesCalculator(){
        ArrayList<ChessMove> PossibleMoves = new ArrayList<>();
        ChessPosition pos = new ChessPosition(position.getRow(), position.getColumn());

        // diagonals
        for (int row = -1, col = -1; position.getColumn() + col > 0 && position.getRow() + row > 0; row--, col--){
            pos = new ChessPosition(position.getRow() + row, position.getColumn() + col);
            if (board.getPiece(pos) == null || OtherColor(pos)) {
                ChessMove move = new ChessMove(position, pos, null );
                PossibleMoves.add(move);
            }
            if (board.getPiece(pos) != null) break;
        }
        for (int row = +1, col = -1; position.getColumn() + col > 0 && position.getRow() + row <= 8; row++, col--){
            pos = new ChessPosition(position.getRow() + row, position.getColumn() + col);
            if (board.getPiece(pos) == null || OtherColor(pos)) {
                ChessMove move = new ChessMove(position, pos, null );
                PossibleMoves.add(move);
            }
            if (board.getPiece(pos) != null) break;
        }
        for (int row = +1, col = +1; position.getColumn() + col <= 8 && position.getRow() + row <= 8; row++, col++){
            pos = new ChessPosition(position.getRow() + row, position.getColumn() + col);
            if (board.getPiece(pos) == null || OtherColor(pos)) {
                ChessMove move = new ChessMove(position, pos, null );
                PossibleMoves.add(move);
            }
            if (board.getPiece(pos) != null) break;
        }
        for (int row = -1, col = +1; position.getColumn() + col <= 8 && position.getRow() + row > 0; row--, col++){
            pos = new ChessPosition(position.getRow() + row, position.getColumn() + col);
            if (board.getPiece(pos) == null || OtherColor(pos)) {
                ChessMove move = new ChessMove(position, pos, null );
                PossibleMoves.add(move);
            }
            if (board.getPiece(pos) != null) break;
        }



        return PossibleMoves;
    }

    public ArrayList<ChessMove> QueenMovesCalculator(){
        var PossibleMoves = new ArrayList<ChessMove>();
        ChessPosition pos = new ChessPosition(position.getRow(), position.getColumn());
        // To the left now y'all
        for (var row = -1; position.getRow() + row > 0; row--) {
            pos = new ChessPosition(position.getRow() + row, position.getColumn());
            if (board.getPiece(pos) == null || OtherColor(pos)) {
                ChessMove move = new ChessMove(position, pos, null );
                PossibleMoves.add(move);
            }
            if (board.getPiece(pos) != null) break;
        }
        for (var row = +1; position.getRow() + row <= 8; row++) {
            pos = new ChessPosition(position.getRow() + row, position.getColumn());
            if (board.getPiece(pos) == null || OtherColor(pos)) {
                ChessMove move = new ChessMove(position, pos, null );
                PossibleMoves.add(move);
            }
            if (board.getPiece(pos) != null) break;
        }
        for (var col = -1; position.getColumn() + col > 0; col--) {
            pos = new ChessPosition( position.getRow(), position.getColumn() + col);
            if (board.getPiece(pos) == null || OtherColor(pos)) {
                ChessMove move = new ChessMove(position, pos, null );
                PossibleMoves.add(move);
            }
            if (board.getPiece(pos) != null) break;
        }
        for (var col = 1; position.getColumn() + col <= 8; col++) {
            pos = new ChessPosition(position.getRow(), position.getColumn() + col);
            if (board.getPiece(pos) == null || OtherColor(pos)) {
                ChessMove move = new ChessMove(position, pos, null );
                PossibleMoves.add(move);
            }
            if (board.getPiece(pos) != null) break;
        }
        // diagonals
        for (int row = -1, col = -1; position.getColumn() + col > 0 && position.getRow() + row > 0; row--, col--){
            pos = new ChessPosition(position.getRow() + row, position.getColumn() + col);
            if (board.getPiece(pos) == null || OtherColor(pos)) {
                ChessMove move = new ChessMove(position, pos, null );
                PossibleMoves.add(move);
            }
            if (board.getPiece(pos) != null) break;
        }
        for (int row = +1, col = -1; position.getColumn() + col > 0 && position.getRow() + row <= 8; row++, col--){
            pos = new ChessPosition(position.getRow() + row, position.getColumn() + col);
            if (board.getPiece(pos) == null || OtherColor(pos)) {
                ChessMove move = new ChessMove(position, pos, null );
                PossibleMoves.add(move);
            }
            if (board.getPiece(pos) != null) break;
        }
        for (int row = +1, col = +1; position.getColumn() + col <= 8 && position.getRow() + row <= 8; row++, col++){
            pos = new ChessPosition(position.getRow() + row, position.getColumn() + col);
            if (board.getPiece(pos) == null || OtherColor(pos)) {
                ChessMove move = new ChessMove(position, pos, null );
                PossibleMoves.add(move);
            }
            if (board.getPiece(pos) != null) break;
        }
        for (int row = -1, col = +1; position.getColumn() + col <= 8 && position.getRow() + row > 0; row--, col++){
            pos = new ChessPosition(position.getRow() + row, position.getColumn() + col);
            if (board.getPiece(pos) == null || OtherColor(pos)) {
                ChessMove move = new ChessMove(position, pos, null );
                PossibleMoves.add(move);
            }
            if (board.getPiece(pos) != null) break;
        }



        return PossibleMoves;
    }

    public ArrayList<ChessMove> PawnMoves(ChessGame.TeamColor teamColor){
        ChessGame.TeamColor opposite = ChessGame.TeamColor.BLACK;
        int modifier1 = 1;
        int modifier2 = 2;
        int startRow = 2;
        int endRow = 7;
        if (teamColor == ChessGame.TeamColor.BLACK) {
            opposite = ChessGame.TeamColor.WHITE;
            modifier1 = -1;
            modifier2 = -2;
            startRow = 7;
            endRow = 2;
        }
        var PossibleMoves = new ArrayList<ChessMove>();
        ChessPosition pos = new ChessPosition(position.getRow() + modifier1, position.getColumn());
        // creating a temporary promotion variable
        ChessPiece.PieceType promotion = null;


        if (board.getPiece(pos) == null) {
            if (position.getRow() == endRow) {
                PossibleMoves.add(new ChessMove(position, pos, ChessPiece.PieceType.QUEEN));
                PossibleMoves.add(new ChessMove(position, pos, ChessPiece.PieceType.BISHOP));
                PossibleMoves.add(new ChessMove(position, pos, ChessPiece.PieceType.ROOK));
                PossibleMoves.add(new ChessMove(position, pos, ChessPiece.PieceType.KNIGHT));
            } else {
                PossibleMoves.add(new ChessMove(position, pos, null));
            }


            pos = new ChessPosition(position.getRow() + modifier2, position.getColumn());
            if (position.getRow() == startRow && board.getPiece(pos) == null) {
                ChessMove move2 = new ChessMove(position, pos, null);
                PossibleMoves.add(move2);
            }
        }
        pos = new ChessPosition(position.getRow() + modifier1, position.getColumn() + 1);
        if (inBounds(pos) && board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() == opposite) {
            if (position.getRow() == endRow) {
                PossibleMoves.add(new ChessMove(position, pos, ChessPiece.PieceType.QUEEN));
                PossibleMoves.add(new ChessMove(position, pos, ChessPiece.PieceType.BISHOP));
                PossibleMoves.add(new ChessMove(position, pos, ChessPiece.PieceType.ROOK));
                PossibleMoves.add(new ChessMove(position, pos, ChessPiece.PieceType.KNIGHT));
            } else {
                PossibleMoves.add(new ChessMove(position, pos, promotion));
            }

        }
        pos = new ChessPosition(position.getRow() + modifier1, position.getColumn() - 1);
        if (inBounds(pos) && board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() == opposite){
            if (position.getRow() == endRow) {
                PossibleMoves.add(new ChessMove(position, pos, ChessPiece.PieceType.QUEEN));
                PossibleMoves.add(new ChessMove(position, pos, ChessPiece.PieceType.BISHOP));
                PossibleMoves.add(new ChessMove(position, pos, ChessPiece.PieceType.ROOK));
                PossibleMoves.add(new ChessMove(position, pos, ChessPiece.PieceType.KNIGHT));
            }
            else{
                PossibleMoves.add(new ChessMove(position, pos, promotion ));
            }
        }
        return PossibleMoves;

    }

    public ArrayList<ChessMove> KnightMoves(){
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        ArrayList<ChessPosition> positions = new ArrayList<>();
        // knight moves in an L in 8 directions GAH!
        positions.add(new ChessPosition(position.getRow() + 2, position.getColumn() - 1));
        positions.add(new ChessPosition(position.getRow() + 2, position.getColumn() + 1));
        positions.add(new ChessPosition(position.getRow() - 2, position.getColumn() + 1));
        positions.add(new ChessPosition(position.getRow() - 2, position.getColumn() - 1));
        positions.add(new ChessPosition(position.getRow() + 1, position.getColumn() - 2));
        positions.add(new ChessPosition(position.getRow() - 1, position.getColumn() - 2));
        positions.add(new ChessPosition(position.getRow() - 1, position.getColumn() + 2));
        positions.add(new ChessPosition(position.getRow() + 1, position.getColumn() + 2));

        for (ChessPosition pos : positions) {
            if (inBounds(pos) && (board.getPiece(pos) == null || OtherColor(pos))) {
                possibleMoves.add(new ChessMove(position, pos, null));
            }
        }
        return possibleMoves;
    }


}
