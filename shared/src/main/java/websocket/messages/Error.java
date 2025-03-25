package websocket.messages;

public class Error extends ServerMessage {

    String error;
    public Error(String error) {
        super(ServerMessageType.ERROR);
        this.error = error;
    }
    String getError() {
        return error;
    }
}
