package server;

import dataaccess.DataAccessException;
import model.GameData;
import service.GameService;
import spark.Response;
import spark.Request;
import com.google.gson.Gson;
import java.util.HashSet;
import com.google.gson.reflect.TypeToken;
/*
 *Handle the errors
 * request objects and response objects
 *
 * list games ✅
 * create game✅💋💋💋💋💋💋💋💋💋💋💋💋💋💋
 * join game
 */

public class GameHandler {

    GameService gameService;
    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object listGames(Request req, Response res) {
        Gson gson = new Gson();
        TypeToken<HashSet<GameData>> typeToken = new TypeToken<HashSet<GameData>>() {};
        HashSet<GameData> games = gson.fromJson(req.body(), typeToken.getType());

        String authToken = new Gson().fromJson(req.headers("Authorization"), String.class);
        HashSet<GameData> gameSet;
        try {
            gameSet = gameService.listGames(authToken);
        }
        catch (DataAccessException e) {
            res.status(401);
            return "{\"message\": \"Error: Unauthorized\" }";
        }
        catch (Exception e) {
            res.status(500);
            return "{\"message\": \"Error: " + e.getMessage() + "\" }";
        }


        res.status(200);
        return gson.toJson(gameSet);
    }

    public Object createGame(Request req, Response res) {
        String gameName = req.queryParams("gameName");
        if (gameName == null || gameName.isEmpty()) {
            res.status(400);
            return "{\"message\": \"Error: Bad Request\"}";
        }
        GameData game = new Gson().fromJson(req.body(), GameData.class);
        String authToken = new Gson().fromJson(req.headers("Authorization"), String.class);
        int gameID;
        try {
            gameID = gameService.createGame(authToken, game.gameName());
        }
        catch (DataAccessException e) {
            res.status(401);
            return "{\"message\": \"Error: Unauthorized\" }";
        }
        catch (Exception e) {
            res.status(500);
            return "{\"message\": \"Error: " + e.getMessage() + "\" }";
        }
        res.status(200);
        return new Gson().toJson(gameID); // turn to Json the game ID
    }

}
