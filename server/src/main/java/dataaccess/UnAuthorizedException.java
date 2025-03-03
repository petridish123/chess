package dataaccess;

public class UnAuthorizedException extends DataAccessException {
    public UnAuthorizedException(String message) {
        super(message);
    }
}
