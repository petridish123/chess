package server.websocket;
import org.eclipse.jetty.websocket.api.Session;
import websocket.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    /*
    TODO: maybe add a group / gameID so that I keep multiple hashmaps Or the hashmap is of hashmaps!!
     */
    public void add(String username, Session session) {
        var connection = new Connection(username, session);
        this.connections.put(username, connection);
    }

    public void remove(String username) {
        this.connections.remove(username);
    }

    /*
    TODO: I need to create a function that broadcasts to a specific group
     */

}
