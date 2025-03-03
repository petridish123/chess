package server;

import com.google.gson.JsonSyntaxException;
import dataaccess.DataAccessException;
import dataaccess.InvalidInput;
import dataaccess.UnAuthorizedException;
import model.GameData;
import service.GameService;
import spark.Response;
import spark.Request;
import com.google.gson.Gson;
import java.util.HashSet;
import java.util.Objects;

/*
 *Handle the errors
 * request objects and response objects
 *
 * list games ✅
 * create game✅💋💋💋💋💋💋💋💋💋💋💋💋💋💋
 * join game  ✅
 */

public class GameHandler {

    GameService gameService;
    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object listGames(Request req, Response res) {


        String authToken = new Gson().fromJson(req.headers("authorization"), String.class);
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
            return "{\"message\": \"Error: \uD83D\uDC8B\uD83D\uDC8B\uD83D\uDC8B\" }";
        }

        GameList gameList = new GameList(gameSet);
        res.status(200);
        return new Gson().toJson(gameList);
    }

    public Object createGame(Request req, Response res) {
        String gameName;
        try {
            GameData game = new Gson().fromJson(req.body(), GameData.class);
            gameName = game.gameName();
        } catch (JsonSyntaxException e) {
            res.status(400);
            return "{\"message\": \"Error: Unauthorized\" }";
        }
        if (gameName == null || gameName.isEmpty()) {
            res.status(400);
            return "{\"message\": \"Error: " + req.queryParams("gameName") + " \" }";
        }
//        GameData game = new Gson().fromJson(req.body(), GameData.class);
        String authToken;
        try {
            authToken = new Gson().fromJson(req.headers("authorization"), String.class);
        } catch (JsonSyntaxException e) {
            res.status(400);
            return "{\"message\": \"Error: " + req.queryParams("gameName") + " \" }";
        }
        int gameID;
        try {
            gameID = gameService.createGame(authToken, gameName);
        }
        catch (DataAccessException e) {
            res.status(401);
            return "{\"message\": \"Error: Unauthorized\" }";
        }
        catch (Exception e) {
            res.status(500);
            return "{\"message\": \"Error: " + e.getMessage() + "\" }";
        }
        GameID ID = new GameID(gameID);
        res.status(200);
        return new Gson().toJson(ID); // turn to Json the game ID
    }

    public Object joinGame(Request req, Response res) {
        String authToken;
        int ID;
        String Color;
        GameRequest color_id;
        try {
            authToken = new Gson().fromJson(req.headers("authorization"), String.class);
            color_id = new Gson().fromJson(req.body(), GameRequest.class);
            Color = color_id.playerColor();
            ID = color_id.gameID();
        } catch (JsonSyntaxException e) {
            res.status(401);
            return "{\"message\": \"Error: Invalid request\" }";
        } try{
            gameService.joinGame(authToken, ID, Color);
        }catch (InvalidInput e){
            res.status(400);
            return "{\"message\": \"Error: Forbidden\" }";
        }catch (UnAuthorizedException e){
            res.status(403);
            return "{\"message\": \"Error: Forbidden\" }";
        }
        catch (DataAccessException e) {
            res.status(400);
            return "{\"message\": \"Error: "+e.getMessage()+" \" }";
        }
        catch (Exception e) {
            res.status(500);
            return "{\"message\": \"Error: " + e.getMessage() + "\" }";
        }

        res.status(200);
        return "{}";
    }

}
