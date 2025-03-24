package dataaccess;

public class BadAuth extends DataAccessException {
    public BadAuth(String message) {
        super(message);
    }
}
