package server.websocket;



import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
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
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
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

    private void handleResign(Session session, Resign command) {
    }

    private void handleLeave(Session session, Leave command) {
    }

    private void handleMakeMove(Session session, MakeMove command) {
    }

    private void handleConnect(Session session, Connect connect) throws IOException {
        Server.gameSessions.put(session, connect.getGameID());
        // if game color == null  then observer
        boolean observer = connect.getTeamColor() == null;
        String message;
        String username = connect.getUsername();
        if (observer) {
            message = username + " has joined the game as an observer";
        }
        else {
            String team = connect.getTeamColor() == ChessGame.TeamColor.WHITE ? "white" : "black";
            message = username + " has joined the game as " + team;
        }
        broadcastMessage(session, new Notification(message));
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



}
