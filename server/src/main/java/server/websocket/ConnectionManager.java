package server.websocket;
import org.eclipse.jetty.websocket.api.Session;
import websocket.*;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    /*
    TODO: maybe add a group / gameID so that I keep multiple hashmaps Or the hashmap is of hashmaps!!
    TODO: Make multiple connection managers 1: for each game!
     */
    public void add(String username, Session session) {
        var connection = new Connection(username, session);
        this.connections.put(username, connection);
    }

    public void remove(String username) {
        this.connections.remove(username);
    }

    /*

     */
    public void broadcastNotification(String excludeVisitorName, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.username.equals(excludeVisitorName)) {
                    c.send(notification.toString());
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.username);
        }
    }

}
