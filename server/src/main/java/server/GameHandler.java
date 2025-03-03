package server;

import service.GameService;
import spark.Response;
import spark.Request;
import com.google.gson.Gson;
/*
 *Handle the errors
 * request objects and response objects
 *
 * list games
 * create game
 * join game
 */

public class GameHandler {

    GameService gameService;
    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object createGame(Request req, Response res) {
        gameService.
        return new Gson().toJson(); // turn to Json the game ID
    }

}
