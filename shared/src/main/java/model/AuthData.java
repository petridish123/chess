package model;

public record AuthData(String authToken, String username) {
    public AuthData(String authToken) {
        this(authToken, null);
    }
}
