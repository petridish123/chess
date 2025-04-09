package websocket.messages;

public class Error extends ServerMessage {

    String errorMessage;
    public Error(String error) {
        super(ServerMessageType.ERROR);
        this.errorMessage = error;
    }
    public String getError() {
        return errorMessage;
    }
}
