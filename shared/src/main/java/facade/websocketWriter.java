package facade;

import javax.websocket.*;
import com.google.gson.Gson;
import exception.ResponseException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class websocketWriter extends Endpoint {

    Session session;

    public websocketWriter(String serverDomain) throws Exception {
        try {
            URI uri = new URI("ws://" + serverDomain + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    handleMessage(message);
                }
            });

        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new Exception();
        }

    }


    private void handleMessage(String message) {
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
