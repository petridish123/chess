package server;

import dataaccess.*;
import org.eclipse.jetty.websocket.api.Session;
import server.websocket.WebSocketHandler;
import service.GameService;
import service.UserService;
import spark.*;
import websocket.*;
import java.util.concurrent.ConcurrentHashMap;

public class Server{
    public static UserService userService;
    public static GameService gameService;
    UserDataAccess userDAO;
    AuthTokenDataAccess authTokenDAO;
    GameDataAccess gameDAO;
    GameHandler gameHandler;
    UserHandler userHandler;
    WebSocketHandler webSocketHandler;
    public static ConcurrentHashMap<Session, Integer> gameSessions = new ConcurrentHashMap<>();


    public Server() {
        try{
            DatabaseManager.createDatabase();
        }catch (DataAccessException e){
            throw new RuntimeException(e);
        }
        try {
            this.gameDAO = new MySqlGameDAO(); // Change these
            this.authTokenDAO = new MySqlAuthDAO();
            this.userDAO = new MySqlUserDao();
            userService = new UserService(userDAO, authTokenDAO);
            gameService = new GameService(gameDAO, authTokenDAO);
            this.gameHandler = new GameHandler(gameService);
            this.userHandler = new UserHandler(userService);
            this.webSocketHandler = new WebSocketHandler();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }



    public int run(int desiredPort) {

        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.webSocket("/ws", webSocketHandler);
        //This line initializes the server and can be removed once you have a functioning endpoint

        Spark.delete("/db", this::clear);
        Spark.post("/user", this.userHandler::register);
        Spark.post("/session", this.userHandler::login);
        Spark.delete("/session", this.userHandler::logout);
        Spark.get("/game", this.gameHandler::listGames);
        Spark.post("/game", this.gameHandler::createGame);
        Spark.put("/game", this.gameHandler::joinGame);


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public Object clear(Request req, Response res) {
        this.gameService.clear();
        this.userService.clear();
        res.status(200);
        return "{}";
    }
}
