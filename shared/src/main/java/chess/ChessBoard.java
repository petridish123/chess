package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Cloneable {
    private ChessPiece[][] board = new ChessPiece[8][8];
    public ChessBoard() {

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    private void setPawns(int row, ChessGame.TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            addPiece(new ChessPosition(row,i), new ChessPiece(teamColor, ChessPiece.PieceType.PAWN));
        }
    }

    private void setRow(int row, ChessGame.TeamColor teamColor) {
        addPiece(new ChessPosition(row,1), new ChessPiece(teamColor, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(row,8), new ChessPiece(teamColor, ChessPiece.PieceType.ROOK));

        addPiece(new ChessPosition(row,2), new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(row,7), new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT));

        addPiece(new ChessPosition(row,3), new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(row,6), new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP));

        addPiece(new ChessPosition(row,4), new ChessPiece(teamColor, ChessPiece.PieceType.QUEEN));

        addPiece(new ChessPosition(row,5), new ChessPiece(teamColor, ChessPiece.PieceType.KING));
    }

    public void resetBoard() {
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                board[row][column] = null;
            }
        }
        // Black side
        setPawns(7, ChessGame.TeamColor.BLACK);
        setPawns(2, ChessGame.TeamColor.WHITE);

        setRow(8, ChessGame.TeamColor.BLACK);
        setRow(1, ChessGame.TeamColor.WHITE);


    }

    @Override
    public ChessBoard clone() {
        ChessBoard clone = new ChessBoard();
        for (int i = 0; i < board.length ; i++) {
            clone.board[i] = Arrays.copyOf(board[i], board[i].length);
        }
        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
