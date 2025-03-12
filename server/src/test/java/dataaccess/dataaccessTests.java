package dataaccess;
import org.junit.jupiter.api.*;
import service.GameService;
import service.UserService;

public class dataaccessTests {

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
    void testCreateGame() throws DataAccessException {}

    @Test
    void testJoin() throws DataAccessException {}

    @Test
    void testListGames() throws DataAccessException {}

    @Test
    void testGetGame() throws DataAccessException {}




}
