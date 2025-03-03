package service;

import org.junit.jupiter.api.*;
import service.*;
import model.*;
import dataaccess.*;
import server.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ChessServiceTest {
    private static final AuthTokenDataAccess authDao = new MemoryAuthTokenDAO();
    private static final GameDataAccess gameDao = new MemoryGameDAO();
    private static final UserDataAccess userDao = new MemoryUserDAO();
    static final GameService gameService = new GameService(gameDao, authDao);
    static final UserService userService = new UserService(userDao, authDao);

    @BeforeEach
    void clear(){
        gameService.clear();
        userService.clear();
    }

    @Test
    void register() throws DataAccessException{
        var authData = userService.registerUser(new UserData("username", "password"));
        assert(authData != null);
    }
    @Test
    void registerNeg() throws DataAccessException{
        var authData = userService.registerUser(new UserData("username", "password"));
        assertThrows(DataAccessException.class, () -> userService.registerUser(new UserData("username", "password")));
    }

    @Test
    void login() throws DataAccessException{
        userService.registerUser(new UserData("username", "password"));
        var auth = userService.loginUser(new UserData("username", "password"));
        assert(auth != null);
    }
    @Test
    void loginNeg() throws DataAccessException{
        userService.registerUser(new UserData("username", "password"));
        assertThrows(DataAccessException.class, () -> userService.loginUser(new UserData("username", "pas")));
    }

    @Test
    void logout() throws DataAccessException{
        userService.registerUser(new UserData("username", "password"));
        var auth = userService.loginUser(new UserData("username", "password"));
        userService.logoutUser(auth.authToken());
    }
    @Test
    void logoutNeg() throws DataAccessException{
        userService.registerUser(new UserData("username", "password"));
        var auth = userService.loginUser(new UserData("username", "password"));
        userService.logoutUser(auth.authToken());
        assertThrows(DataAccessException.class, () -> userService.logoutUser(auth.authToken()));
    }

    @Test
    void createGames() throws DataAccessException {
        userService.registerUser(new UserData("username", "password"));
        var auth = userService.loginUser(new UserData("username", "password"));
        int gameID = gameService.createGame(auth.authToken(), "game1");
        assert(gameService.listGames(auth.authToken()).size() == 1);
    }
    @Test
    void createGamesNeg() throws DataAccessException {
        userService.registerUser(new UserData("username", "password"));
        var auth = userService.loginUser(new UserData("username", "password"));
        int gameID = gameService.createGame(auth.authToken(), "game1");
        assertThrows(DataAccessException.class, () -> gameService.createGame(auth.authToken(), "game1"));
    }

    @Test
    void getGames() throws DataAccessException{
        userService.registerUser(new UserData("username", "password"));
        var auth = userService.loginUser(new UserData("username", "password"));
        int gameID = gameService.createGame(auth.authToken(), "game1");
        assert(gameService.listGames(auth.authToken()).contains( gameService.gameDAO.getGame(gameID)));
    }
    @Test
    void getGamesNeg() throws DataAccessException{
        userService.registerUser(new UserData("username", "password"));
        var auth = userService.loginUser(new UserData("username", "password"));
        userService.logoutUser(auth.authToken());
        assertThrows(DataAccessException.class, () -> gameService.createGame(auth.authToken(), "game1"));
    }

    @Test
    void joinGame() throws DataAccessException{
        userService.registerUser(new UserData("username", "password"));
        var auth = userService.loginUser(new UserData("username", "password"));
        var gameID = gameService.createGame(auth.authToken(), "game1");
        assertDoesNotThrow(() -> gameService.joinGame(auth.authToken(), gameID, "WHITE"));
    }
    @Test
    void joinGameNeg() throws DataAccessException{
        userService.registerUser(new UserData("username", "password"));
        var auth = userService.loginUser(new UserData("username", "password"));
        var gameID = gameService.createGame(auth.authToken(), "game1");
        gameService.joinGame(auth.authToken(), gameID, "WHITE");
        assertThrows(DataAccessException.class, () -> gameService.joinGame(auth.authToken(), gameID, "WHITE"));
    }
    @Test
    void clearGames() throws DataAccessException{
        userService.registerUser(new UserData("username", "password"));
        var auth = userService.loginUser(new UserData("username", "password"));
        var gameID = gameService.createGame(auth.authToken(), "game1");
        gameService.clear();
        assertThrows(DataAccessException.class, () -> gameService.joinGame(auth.authToken(), gameID, "WHITE"));
    }





}
