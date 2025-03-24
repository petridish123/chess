package dataaccess;
import org.junit.jupiter.api.*;
import Handler.Server;
import service.GameService;
import service.UserService;

public class DataaccessTests {

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
    private static Server server;
    @BeforeAll
    public static void startTestrun() {
        server = new Server();
        var port = server.run(0);

    }

    @AfterAll
    static void stopServerTests() {
        server.stop();
    }

    @Test
    void testCreateGame() throws DataAccessException {}

    @Test
    void testJoin() throws DataAccessException {}

    @Test
    void testListGames() throws DataAccessException {}

    @Test
    void testGetGame() throws DataAccessException {}

    @Test
    void testUpdateGame() throws DataAccessException {}

    @Test
    void testDeleteGame() throws DataAccessException {}

    @Test
    void testCreateGameNEG() throws DataAccessException {}

    @Test
    void testJoinNEG() throws DataAccessException {}

    @Test
    void testListGamesNEG() throws DataAccessException {}

    @Test
    void testGetGameNEG() throws DataAccessException {}

    @Test
    void testUpdateGameNEG() throws DataAccessException {}

    @Test
    void testDeleteGameNEG() throws DataAccessException {}

    @Test
    void clearDB() throws DataAccessException {}

    @Test
    void login() throws DataAccessException {}

    @Test void logout() throws DataAccessException {}

    @Test void getUser() throws DataAccessException {}

    @Test void registerUser() throws DataAccessException {}


    @Test
    void loginNEG() throws DataAccessException {}

    @Test void logoutNEG() throws DataAccessException {}

    @Test void getUserNEG() throws DataAccessException {}

    @Test void registerUserNEG() throws DataAccessException {}



}
