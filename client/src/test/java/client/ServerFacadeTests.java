package client;

import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void login() {}

    @Test
    public void loginNEG() {}

    @Test
    public void logout() {}

    @Test
    public void logoutNEG() {}

    @Test
    public void register() {}

    @Test
    public void registerNEG() {}

    @Test
    public void create(){

    }

    @Test
    public void createNEG() {}

    @Test
    public void join(){}

    @Test
    public void joinNEG() {}

    @Test
    public void list() {}

    @Test
    public void listNEG() {}



}
