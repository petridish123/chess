package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String username;
    public Session session;

    public Connection(String username, Session session) {
        this.username = username;
        this.session = session;
    }

    public void send(String msg) throws IOException {// May need to modify in order to send the right thing
        var new_msg = new Gson().toJson(msg);
        session.getRemote().sendString(new_msg);
    }
}
