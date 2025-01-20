package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard board;
    TeamColor teamColorTurn;
    public ChessGame() {
        this.board = new ChessBoard();
        this.teamColorTurn = TeamColor.WHITE;
        this.board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamColorTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamColorTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        ArrayList<ChessMove> moves = new ArrayList<>();
        if (piece == null) {
            return null;
        }
        Collection<ChessMove> pieceMove = piece.pieceMoves(board, startPosition);
        for (ChessMove move : pieceMove) {
            if (validateMove(move)) {
                moves.add(move);
            }
        }
        return moves; // needs to check if this team is in check.
    }

    public boolean validateMove(ChessMove move) {

        ChessBoard boardy = this.board.clone();
        // make move and check if in check
        ChessPiece piece = board.getPiece(move.getStartPosition());
        board.addPiece(move.getEndPosition(), piece);
        board.addPiece(move.getStartPosition(), null);
        boolean check = !isInCheck(piece.getTeamColor());
        board = boardy;
        return check;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        Collection<ChessMove> moves = validMoves(move.getStartPosition());
        if (piece == null || !moves.contains(move) || teamColorTurn != piece.getTeamColor()) {
            throw new InvalidMoveException();
        }

        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null) {
            piece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
        }
        board.addPiece(move.getEndPosition(), piece);
        board.addPiece(move.getStartPosition(), null);
        setTeamTurn(OppositeTeamColor(teamColorTurn)); // get valid moves and check that first
    }

    public TeamColor OppositeTeamColor(TeamColor teamColor) {
        return teamColor == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
    }
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPiece piece;
        for (var i = 1; i <= 8; i++) {
            for (var j = 1; j <= 8; j++) {
                ChessPosition pos= new ChessPosition(i, j);
                piece = board.getPiece(pos);
                if (piece != null && piece.getTeamColor() != teamColor) {
                    for (ChessMove move : piece.pieceMoves(board, pos)) {
                        if (KingFound(move)) {
                            return true;
                        }
                    }
                }

            }
        }
        return false;
    }

    public boolean KingFound(ChessMove move) {
        return board.getPiece(move.getEndPosition()) != null && board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING;
    }





    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean mate;
        if (isInCheck(teamColor)) {
            mate = true;
            for (var i = 1; i <= 8; i++) {
                for (var j = 1; j <= 8; j++) {
                    ChessPosition pos= new ChessPosition(i, j);
                    ChessPiece piece = board.getPiece(pos);
                    if (piece != null && piece.getTeamColor() == teamColor) {
                        mate = mate && validMoves(pos).isEmpty();
                    }

                }
            }
        }
        else mate = false;
        return mate;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
//        this.board = new ChessBoard();
//        board.resetBoard();
        return this.board;
    }
}
