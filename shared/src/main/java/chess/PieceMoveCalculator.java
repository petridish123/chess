package chess;

import java.util.ArrayList;

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
            return kingMovesCalculator();
        }
        if (type == ChessPiece.PieceType.QUEEN) {
            return queenMovesCalculator();
        }
        if (type == ChessPiece.PieceType.BISHOP) {
            return bishopMovesCalculator();
        }
        if (type == ChessPiece.PieceType.KNIGHT) {
            return knightMoves();
        }
        if (type == ChessPiece.PieceType.PAWN) {
            if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                return pawnMoves(ChessGame.TeamColor.BLACK);
            }

            return pawnMoves(ChessGame.TeamColor.WHITE);
        }
        if (type == ChessPiece.PieceType.ROOK) {
            return rookMovesCalculator();
        }
        return new ArrayList<ChessMove>();
    }


    public boolean inBounds(ChessPosition pos){
        return pos.getColumn() > 0 && pos.getColumn() <= 8 && pos.getRow() > 0 && pos.getRow() <= 8;
    }
    public boolean otherColor(ChessPosition pos){
        return board.getPiece(pos).getTeamColor() != piece.getTeamColor();
    }


    public ArrayList<ChessMove> kingMovesCalculator(){
        var possibleMoves = new ArrayList<ChessMove>();
        for (var row = -1; row < 2; row++) {
            for (var col = -1; col < 2; col++) {
                ChessPosition pos = new ChessPosition(position.getRow() + row, position.getColumn()+col);
                if (inBounds(pos) && (board.getPiece(pos) == null || otherColor(pos))) {
                    ChessMove move = new ChessMove(position, pos, null );
                    possibleMoves.add(move);
                }
            }
        }
        return possibleMoves;
    }

    public ArrayList<ChessMove> rookMovesCalculator(){
        var possibleMoves = new ArrayList<ChessMove>();
        ChessPosition pos;
        // To the left now y'all
        for (var row = -1; position.getRow() + row > 0; row--) {
            pos = new ChessPosition(position.getRow() + row, position.getColumn());
            if (board.getPiece(pos) == null || otherColor(pos)) {
                ChessMove move = new ChessMove(position, pos, null );
                possibleMoves.add(move);
            }
            if (board.getPiece(pos) != null){
                break;
            }
        }
        for (var row = +1; position.getRow() + row <= 8; row++) {
            pos = new ChessPosition(position.getRow() + row, position.getColumn());
            if (board.getPiece(pos) == null || otherColor(pos)) {
                ChessMove move = new ChessMove(position, pos, null );
                possibleMoves.add(move);
            }
            if (board.getPiece(pos) != null){
                break;
            }
        }
        for (var col = -1; position.getColumn() + col > 0; col--) {
            pos = new ChessPosition( position.getRow(), position.getColumn() + col);
            if (board.getPiece(pos) == null || otherColor(pos)) {
                ChessMove move = new ChessMove(position, pos, null );
                possibleMoves.add(move);
            }
            if (board.getPiece(pos) != null){
                break;
            }
        }
        for (var col = 1; position.getColumn() + col <= 8; col++) {
            pos = new ChessPosition(position.getRow(), position.getColumn() + col);
            if (board.getPiece(pos) == null || otherColor(pos)) {
                ChessMove move = new ChessMove(position, pos, null );
                possibleMoves.add(move);
            }
            if (board.getPiece(pos) != null){
                break;
            }
        }

        return possibleMoves;
    }

    public ArrayList<ChessMove> bishopMovesCalculator(){
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        ChessPosition pos;

        // diagonals
        for (int row = -1, col = -1; position.getColumn() + col > 0 && position.getRow() + row > 0; row--, col--){
            if (diagonals(possibleMoves, row, col)) {
                break;
            }
        }
        for (int row = +1, col = -1; position.getColumn() + col > 0 && position.getRow() + row <= 8; row++, col--){
            if (diagonals(possibleMoves, row, col)) {
                break;
            }
        }
        for (int row = +1, col = +1; position.getColumn() + col <= 8 && position.getRow() + row <= 8; row++, col++){
            if (diagonals(possibleMoves, row, col)) {
                break;
            }
        }
        for (int row = -1, col = +1; position.getColumn() + col <= 8 && position.getRow() + row > 0; row--, col++){
            if (diagonals(possibleMoves, row, col)) {
                break;
            }
        }



        return possibleMoves;
    }

    private boolean diagonals(ArrayList<ChessMove> possibleMoves, int row, int col) {
        ChessPosition pos;
        pos = new ChessPosition(position.getRow() + row, position.getColumn() + col);
        if (board.getPiece(pos) == null || otherColor(pos)) {
            ChessMove move = new ChessMove(position, pos, null );
            possibleMoves.add(move);
        }
        return board.getPiece(pos) != null;
    }

    public ArrayList<ChessMove> queenMovesCalculator(){
        var possibleMoves = new ArrayList<ChessMove>();
        possibleMoves.addAll(rookMovesCalculator());
        possibleMoves.addAll(bishopMovesCalculator());
        return possibleMoves;
    }

    public ArrayList<ChessMove> pawnMoves(ChessGame.TeamColor teamColor){
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
        var possibleMoves = new ArrayList<ChessMove>();
        ChessPosition pos = new ChessPosition(position.getRow() + modifier1, position.getColumn());
        // creating a temporary promotion variable
        ChessPiece.PieceType promotion = null;


        if (board.getPiece(pos) == null) {
            if (position.getRow() == endRow) {
                possibleMoves.add(new ChessMove(position, pos, ChessPiece.PieceType.QUEEN));
                possibleMoves.add(new ChessMove(position, pos, ChessPiece.PieceType.BISHOP));
                possibleMoves.add(new ChessMove(position, pos, ChessPiece.PieceType.ROOK));
                possibleMoves.add(new ChessMove(position, pos, ChessPiece.PieceType.KNIGHT));
            } else {
                possibleMoves.add(new ChessMove(position, pos, null));
            }


            pos = new ChessPosition(position.getRow() + modifier2, position.getColumn());
            if (position.getRow() == startRow && board.getPiece(pos) == null) {
                ChessMove move2 = new ChessMove(position, pos, null);
                possibleMoves.add(move2);
            }
        }
        pos = new ChessPosition(position.getRow() + modifier1, position.getColumn() + 1);
        if (inBounds(pos) && board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() == opposite) {
            if (position.getRow() == endRow) {
                possibleMoves.add(new ChessMove(position, pos, ChessPiece.PieceType.QUEEN));
                possibleMoves.add(new ChessMove(position, pos, ChessPiece.PieceType.BISHOP));
                possibleMoves.add(new ChessMove(position, pos, ChessPiece.PieceType.ROOK));
                possibleMoves.add(new ChessMove(position, pos, ChessPiece.PieceType.KNIGHT));
            } else {
                possibleMoves.add(new ChessMove(position, pos, promotion));
            }

        }
        pos = new ChessPosition(position.getRow() + modifier1, position.getColumn() - 1);
        if (inBounds(pos) && board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() == opposite){
            if (position.getRow() == endRow) {
                possibleMoves.add(new ChessMove(position, pos, ChessPiece.PieceType.QUEEN));
                possibleMoves.add(new ChessMove(position, pos, ChessPiece.PieceType.BISHOP));
                possibleMoves.add(new ChessMove(position, pos, ChessPiece.PieceType.ROOK));
                possibleMoves.add(new ChessMove(position, pos, ChessPiece.PieceType.KNIGHT));
            }
            else{
                possibleMoves.add(new ChessMove(position, pos, promotion ));
            }
        }
        return possibleMoves;

    }

    public ArrayList<ChessMove> knightMoves(){
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
            if (inBounds(pos) && (board.getPiece(pos) == null || otherColor(pos))) {
                possibleMoves.add(new ChessMove(position, pos, null));
            }
        }
        return possibleMoves;
    }


}
