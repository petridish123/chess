package websocket.messages;

public class Notification extends ServerMessage {
    String message;
    public Notification(String notification) {
        super(ServerMessageType.NOTIFICATION);
        this.message = notification;
    }
    public String getNotification() {
        return message;
    }


}
