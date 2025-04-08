package server.websocket;



import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.*;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.Connect;
import websocket.commands.MakeMove;
import websocket.commands.UserGameCommand;
import websocket.messages.*;
import websocket.commands.*;
import websocket.messages.Error;


import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WebSocketHandler {




    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        System.out.println(message);
        if (message.contains("\"commandType\":\"CONNECT\"")){
            Connect connect = new Gson().fromJson(message, Connect.class);
            handleConnect(session, connect);
        }
        else if (message.contains("\"commandType\":\"MAKE_MOVE\"")) {
            MakeMove makemove = new Gson().fromJson(message, MakeMove.class);
            handleMakeMove(session, makemove);
        }
        else if (message.contains("\"commandType\":\"LEAVE\"")) {
            Leave leave = new Gson().fromJson(message, Leave.class);
            handleLeave(session, leave);
        }
        else if (message.contains("\"commandType\":\"RESIGN\"")) {
            Resign resign = new Gson().fromJson(message, Resign.class);
            handleResign(session, resign);
        }
    }

<<<<<<< Updated upstream
    private void handleResign(Session session, Resign command) {
        try {
            GameData game = Server.gameService.getGame(command.getAuthToken(), command.getGameID());
            AuthData auth = Server.userService.getAuth(command.getAuthToken());
            ChessGame.TeamColor color = getTeamColor(auth.username(), game);

            game.game().setOver(true);
            broadcastMessage(session, new Notification(auth.username() + "Has resigned"));
            Server.gameService.updateGame(game);
        } catch (DataAccessException e) {
            return;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void handleLeave(Session session, Leave command) {
        try{
            GameData game = Server.gameService.getGame(command.getAuthToken(), command.getGameID());
            AuthData auth = Server.userService.getAuth(command.getAuthToken());
            ChessGame.TeamColor color = getTeamColor(auth.username(),game);
            Server.gameService.leaveGame(command.getAuthToken(), command.getGameID(),color.toString());
=======
    private void handleLeave(Session session, Leave command) throws IOException {
        try {
            AuthData auth = Server.userService.getAuth(command.getAuthString());

            Notification notif = new Notification("%s has left the game".formatted(auth.username()));
            broadcastMessage(session, notif);

            session.close();
        } catch (UnauthorizedException e) {
            sendError(session, new Error("Error: Not authorized"));
        }
    }

    private void handleResign(Session session, Resign command) throws IOException {
        try {
            AuthData auth = Server.userService.get(command.getAuthToken());
            GameData game = Server.gameService.getGame(command.getAuthToken(), command.getGameID());
            ChessGame.TeamColor userColor = getTeamColor(auth.username(), game);

            String opponentUsername = userColor == ChessGame.TeamColor.WHITE ? game.blackUsername() : game.whiteUsername();

            if (userColor == null) {
                sendError(session, new Error("Error: You are observing this game"));
                return;
            }

            if (game.game().over()) {
                sendError(session, new Error("Error: The game is already over!"));
                return;
            }

            game.game().setGameOver(true);
            Server.gameService.updateGame(auth.authToken(), game);
            Notification notif = new Notification("%s has forfeited, %s wins!".formatted(auth.username(), opponentUsername));
            broadcastMessage(session, notif, true);
        } catch (UnauthorizedException e) {
            sendError(session, new Error("Error: Not authorized"));
        } catch (BadRequestException e) {
            sendError(session, new Error("Error: invalid game"));
>>>>>>> Stashed changes
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleMakeMove(Session session, MakeMove command) throws IOException {

        try {
            GameData game = Server.gameService.getGame(command.getAuthToken(), command.getGameID());
            AuthData auth = Server.userService.getAuth(command.getAuthToken());
            ChessGame.TeamColor color = getTeamColor(auth.username(),game);
            if (game.game().isOver()){
                sendError(session,new Error("The game is over you cannot make a move!"));
                return;
            }
            Boolean turn = false;
            if (Objects.equals(game.whiteUsername(), auth.username()) && game.game().getTeamTurn() == ChessGame.TeamColor.WHITE){
                turn = true;
            }else if (Objects.equals(game.blackUsername(), auth.username()) && game.game().getTeamTurn() == ChessGame.TeamColor.BLACK){
                turn = true;
            }

            if (turn){
                game.game().makeMove(command.getMove());
                Server.gameService.updateGame(game);
                System.out.println(game);
                broadcastMessage(session, new Notification(auth.username() + " has made a move!"));
                broadcastMessage(session, new LoadGame(game.game()), true);
            }else{
                sendError(session,new Error("It is not your turn!"));
                System.out.println(game.game().getTeamTurn());
                System.out.println(game);

            }
            if (game.game().isInCheckmate(game.game().oppositeTeamColor(color))){
                broadcastMessage(session, new Notification(game.game().oppositeTeamColor(color).toString() + "is in checkmate!"));
                game.game().setOver(true);
                Server.gameService.updateGame(game);
            }else if (game.game().isInCheck(game.game().oppositeTeamColor(color))){
                broadcastMessage(session, new Notification(game.game().oppositeTeamColor(color).toString() + "Is in check!"));
            }

        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            sendError(session,new Error(e.getMessage()));
            throw new RuntimeException(e);
        } catch (InvalidMoveException e) {
            System.out.println(e.getMessage() + " error with the move");
            sendError(session, new Error("Invalid move. Check your start and end positions (promotion piece might be mistyped). "));
        }

        /*
        Steps I need to do:
        get the move

         */
    }

    private void handleConnect(Session session, Connect connect) throws IOException {
        Server.gameSessions.put(session, connect.getGameID());
        // if game color == null  then observer
        boolean observer = connect.getTeamColor() == null;
        GameData game = null;
        try {
            game = Server.gameService.getGame(connect.getAuthToken(), connect.getGameID());
        } catch (DataAccessException e) {
            sendError(session, new Error(e.getMessage()));
            return;
        }
        assert game != null;
        String message;
        String username = connect.getUsername();
        if (observer) {
            message = username + " has joined the game as an observer";
        }
        else {
            boolean correctColor;
            if (connect.getTeamColor() == ChessGame.TeamColor.WHITE) {
                correctColor = Objects.equals(game.whiteUsername(), connect.getUsername());
            }
            else {
                correctColor = Objects.equals(game.blackUsername(), connect.getUsername());
            }
            if (!correctColor) {
                Error error = new Error("Error: attempting to join with wrong color");
                sendError(session, error);
                return;
            }
            String team = connect.getTeamColor() == ChessGame.TeamColor.WHITE ? "white" : "black";
            message = username + " has joined the game as " + team;


        }
        broadcastMessage(session, new Notification(message));
        broadcastMessage(session, new LoadGame(game.game()), true);

    }







    public void broadcastMessage(Session currSession, ServerMessage message) throws IOException {
        broadcastMessage(currSession, message, false);
    }

    // Send the notification to all clients on the current game
    public void broadcastMessage(Session currSession, ServerMessage message, boolean toSelf) throws IOException {
        System.out.printf("Broadcasting (toSelf: %s): %s%n", toSelf, new Gson().toJson(message));
        for (Session session : Server.gameSessions.keySet()) {
            boolean sameGame = Server.gameSessions.get(session).equals(Server.gameSessions.get(currSession));
            boolean isSelf = session == currSession;
            if ((toSelf || !isSelf)  && sameGame) {
                sendMessage(session, message);
            }
        }
    }

    public void sendMessage(Session session, ServerMessage message) throws IOException {
        session.getRemote().sendString(new Gson().toJson(message));
    }

    private void sendError(Session session, Error error) throws IOException {
        System.out.printf("Error: %s%n", new Gson().toJson(error));
        session.getRemote().sendString(new Gson().toJson(error));
    }

    ChessGame.TeamColor getTeamColor(String username, GameData game) {
        if (game.whiteUsername().equals(username)) {
            return ChessGame.TeamColor.WHITE;
        } else if (game.blackUsername().equals(username)) {
            return ChessGame.TeamColor.BLACK;
        }else{
            return null;
        }
    }



}
