package dataaccess;
import org.junit.jupiter.api.*;
import passoff.model.TestUser;
import passoff.server.TestServerFacade;
import server.Server;
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

    private static final TestUser TEST_USER = new TestUser("ExistingUser", "existingUserPassword", "eu@mail.com");

    private static TestServerFacade serverFacade;

    private static Server server;

    private static Class<?> databaseManagerClass;


    @BeforeAll
    public static void startServer() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        serverFacade = new TestServerFacade("localhost", Integer.toString(port));
    }



    @AfterAll
    static void stopServer() {
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




}
