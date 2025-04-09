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
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
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

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        Server.gameSessions.remove(session);
    }

    private void handleLeave(Session session, Leave command) throws IOException {
            try {


                AuthData auth = Server.userService.getAuth(command.getAuthToken());
                Notification notif = new Notification("%s has left the game".formatted(auth.username()));
                broadcastMessage(session, notif, true);
                Server.gameSessions.remove(session);
                session.close();
            }catch (IOException e){
                System.out.println(e.getMessage());
                sendError(session, new Error("PT 3"));
            }
    }

    private void handleResign(Session session, Resign command){
        try {
            AuthData auth = Server.userService.getAuth(command.getAuthToken());
            GameData game = Server.gameService.getGame(command.getAuthToken(), command.getGameID());
            ChessGame.TeamColor userColor = getTeamColor(auth.username(), game);
            String opponentUsername = userColor == ChessGame.TeamColor.WHITE ? game.blackUsername() : game.whiteUsername();

            if (userColor == null) {
                sendError(session, new Error("Error: You are observing this game"));
                return;
            }
            if (game.game().isOver()) {
                sendError(session, new Error("Error: The game is already over!"));
                return;
            }
            game.game().setOver(true);
            Server.gameService.updateGame(game);
            Notification notif = new Notification("%s has forfeited, %s wins!".formatted(auth.username(), opponentUsername));
            broadcastMessage(session, notif, true);;
        } catch (DataAccessException e) {
            sendError(session, new Error("Error: "));
        }catch (IOException e){
            System.out.println(e.getMessage());
            sendError(session, new Error("Error: IO Errror pt 2 "));
        }
    }

    private void handleMakeMove(Session session, MakeMove command)  {

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
                System.out.println("Turn is white");
            }else if (Objects.equals(game.blackUsername(), auth.username()) && game.game().getTeamTurn() == ChessGame.TeamColor.BLACK){
                turn = true;
                System.out.println("Turn is black");
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
                broadcastMessage(session, new Notification(game.game().oppositeTeamColor(color).toString() + " is in checkmate!"), true);
                game.game().setOver(true);
                Server.gameService.updateGame(game);
            }else if (game.game().isInCheck(game.game().oppositeTeamColor(color))){
                broadcastMessage(session, new Notification(game.game().oppositeTeamColor(color).toString() + " Is in check!"), true);
            }

        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            sendError(session,new Error("e.getMessage()"));
        } catch (InvalidMoveException e) {
            System.out.println(e.getMessage() + " error with the move");
            sendError(session, new Error("Invalid move. Check your start and end positions (promotion piece might be mistyped). "));
        }catch (IOException e){
            System.out.println(e.getMessage());
            sendError(session,new Error("IO exception"));
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
            System.out.println();
            System.out.println(game);

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
            sendMessage(session, new LoadGame(game.game()));
        } catch (DataAccessException e) {
            sendError(session, new Error("e.getMessage()"));
            return;
        }catch (IOException e){
            System.out.println(e.getMessage());
            sendError(session, new Error("IO exception pt 4"));
        }
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

    private void sendError(Session session, Error error){
        try {
            if (error.getError().equals(null)) {
                error = new Error("Placeholder");
            }
            System.out.printf("Error: %s%n", new Gson().toJson(error));
            session.getRemote().sendString(new Gson().toJson(error));
        }catch (IOException e){
            System.out.println(e.getMessage());

        }
    }

    ChessGame.TeamColor getTeamColor(String username, GameData game) {

        if (!Objects.equals(game.whiteUsername(),null)&&game.whiteUsername().equals(username)) {
            return ChessGame.TeamColor.WHITE;
        } else if (!Objects.equals(game.blackUsername(), null) && game.blackUsername().equals(username)) {
            return ChessGame.TeamColor.BLACK;
        }else{
            return null;
        }
    }



}
