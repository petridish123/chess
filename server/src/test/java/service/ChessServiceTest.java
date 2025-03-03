package service;

import org.junit.jupiter.api.*;
import model.*;
import dataaccess.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ChessServiceTest {
    private static final AuthTokenDataAccess AUTH_DAO = new MemoryAuthTokenDAO();
    private static final GameDataAccess GAME_DAO = new MemoryGameDAO();
    private static final UserDataAccess USER_DAO = new MemoryUserDAO();
    static final GameService GAME_SERVICE = new GameService(GAME_DAO, AUTH_DAO);
    static final UserService USER_SERVICE = new UserService(USER_DAO, AUTH_DAO);

    @BeforeEach
    void clear(){
        GAME_SERVICE.clear();
        USER_SERVICE.clear();
    }

    @Test
    void register() throws DataAccessException{
        var authData = USER_SERVICE.registerUser(new UserData("username", "password"));
        assert(authData != null);
    }
    @Test
    void registerNeg() throws DataAccessException{
        var authData = USER_SERVICE.registerUser(new UserData("username", "password"));
        assertThrows(DataAccessException.class, () -> USER_SERVICE.registerUser(new UserData("username", "password")));
    }

    @Test
    void login() throws DataAccessException{
        USER_SERVICE.registerUser(new UserData("username", "password"));
        var auth = USER_SERVICE.loginUser(new UserData("username", "password"));
        assert(auth != null);
    }
    @Test
    void loginNeg() throws DataAccessException{
        USER_SERVICE.registerUser(new UserData("username", "password"));
        assertThrows(DataAccessException.class, () -> USER_SERVICE.loginUser(new UserData("username", "pas")));
    }

    @Test
    void logout() throws DataAccessException{
        USER_SERVICE.registerUser(new UserData("username", "password"));
        var auth = USER_SERVICE.loginUser(new UserData("username", "password"));
        USER_SERVICE.logoutUser(auth.authToken());
    }
    @Test
    void logoutNeg() throws DataAccessException{
        USER_SERVICE.registerUser(new UserData("username", "password"));
        var auth = USER_SERVICE.loginUser(new UserData("username", "password"));
        USER_SERVICE.logoutUser(auth.authToken());
        assertThrows(DataAccessException.class, () -> USER_SERVICE.logoutUser(auth.authToken()));
    }

    @Test
    void createGames() throws DataAccessException {
        USER_SERVICE.registerUser(new UserData("username", "password"));
        var auth = USER_SERVICE.loginUser(new UserData("username", "password"));
        int gameID = GAME_SERVICE.createGame(auth.authToken(), "game1");
        assert(GAME_SERVICE.listGames(auth.authToken()).size() == 1);
    }
    @Test
    void createGamesNeg() throws DataAccessException {
        USER_SERVICE.registerUser(new UserData("username", "password"));
        var auth = USER_SERVICE.loginUser(new UserData("username", "password"));
        int gameID = GAME_SERVICE.createGame(auth.authToken(), "game1");
        assertThrows(DataAccessException.class, () -> GAME_SERVICE.createGame(auth.authToken(), "game1"));
    }

    @Test
    void getGames() throws DataAccessException{
        USER_SERVICE.registerUser(new UserData("username", "password"));
        var auth = USER_SERVICE.loginUser(new UserData("username", "password"));
        int gameID = GAME_SERVICE.createGame(auth.authToken(), "game1");
        assert(GAME_SERVICE.listGames(auth.authToken()).contains( GAME_SERVICE.gameDAO.getGame(gameID)));
    }
    @Test
    void getGamesNeg() throws DataAccessException{
        USER_SERVICE.registerUser(new UserData("username", "password"));
        var auth = USER_SERVICE.loginUser(new UserData("username", "password"));
        USER_SERVICE.logoutUser(auth.authToken());
        assertThrows(DataAccessException.class, () -> GAME_SERVICE.createGame(auth.authToken(), "game1"));
    }

    @Test
    void joinGame() throws DataAccessException{
        USER_SERVICE.registerUser(new UserData("username", "password"));
        var auth = USER_SERVICE.loginUser(new UserData("username", "password"));
        var gameID = GAME_SERVICE.createGame(auth.authToken(), "game1");
        assertDoesNotThrow(() -> GAME_SERVICE.joinGame(auth.authToken(), gameID, "WHITE"));
    }
    @Test
    void joinGameNeg() throws DataAccessException{
        USER_SERVICE.registerUser(new UserData("username", "password"));
        var auth = USER_SERVICE.loginUser(new UserData("username", "password"));
        var gameID = GAME_SERVICE.createGame(auth.authToken(), "game1");
        GAME_SERVICE.joinGame(auth.authToken(), gameID, "WHITE");
        assertThrows(DataAccessException.class, () -> GAME_SERVICE.joinGame(auth.authToken(), gameID, "WHITE"));
    }
    @Test
    void clearGames() throws DataAccessException{
        USER_SERVICE.registerUser(new UserData("username", "password"));
        var auth = USER_SERVICE.loginUser(new UserData("username", "password"));
        var gameID = GAME_SERVICE.createGame(auth.authToken(), "game1");
        GAME_SERVICE.clear();
        assertThrows(DataAccessException.class, () -> GAME_SERVICE.joinGame(auth.authToken(), gameID, "WHITE"));
    }





}
