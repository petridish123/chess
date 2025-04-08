package facade;

import javax.websocket.*;

import chess.ChessGame;
import com.google.gson.Gson;
import websocket.messages.LoadGame;
import websocket.messages.Notification;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import repls.GameREPL;
import static ui.EscapeSequences.ERASE_LINE;


public class WebsocketWriter extends Endpoint {

    Session session;

    public WebsocketWriter(String serverDomain) throws Exception {
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
        if (message.contains("\"serverMessageType\":\"NOTIFICATION\"")) {
            Notification notif = new Gson().fromJson(message, Notification.class);
            printNotification(notif.getNotification());
        }
        else if (message.contains("\"serverMessageType\":\"ERROR\"")) {
            Error error = new Gson().fromJson(message, Error.class);
            printNotification(error.getMessage());
        }
        else if (message.contains("\"serverMessageType\":\"LOAD_GAME\"")) {
            LoadGame loadGame = new Gson().fromJson(message, LoadGame.class);
            printLoadedGame(loadGame.getGame());
        }
    }

    private void printNotification(String message) {
        System.out.print(ERASE_LINE + '\r');
        System.out.println(message);
    }

    private void printLoadedGame(ChessGame game) {
        System.out.print(ERASE_LINE + "\r\n");
        GameREPL.boardPrinter.updateGame(game);
        ChessGame.TeamColor teamColor = (Objects.equals(GameREPL.color, "WHITE")) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
        GameREPL.boardPrinter.printBoard(teamColor);
        System.out.print("[IN-GAME] >>> ");
    }

    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
