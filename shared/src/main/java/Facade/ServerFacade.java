package Facade;


import com.google.gson.Gson;
//import exception.ErrorResponse;
import exception.ResponseException;
import model.*;
import model.GameList;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Map;

public class ServerFacade {

    public String serverUrl;
    public String authToken = "";
    writer writer;
    public ServerFacade() {
        this("localhost:8080");
    }
    public ServerFacade(String serverURL) {

        this.serverUrl = "http://" + serverURL;
        this.writer =  new writer(this.authToken,this.serverUrl);
    }

    /**
     * NOTE for any function that takes in multiple, you need to turn it into a map
     * TODONE : Create a Login function || takes in a userData and then returns an authData object
     * TODONE : Create a register || Takes in a user data and returns an authData object
     * TODONE : Create a logout || takes in a authtoken string? and returns VOID
     * TODONE : Create a list games || takes in a string authToken and returns a list of gameData
     * TODONE : create a create game || takes in a string auth and a string gameName and returns a game ID
     * TODONE : create  join game ( called play game) || takes in an authToken, player color and gameID
     * TODONE : create a clear function (low priority) || NOTHING RAHHHH
     *
     */
    public boolean login(String username, String password) {
        UserData user = new UserData(username, password);
        try {
            AuthData auth = this.writer.makeRequest("POST", "/session", user, AuthData.class);
            this.authToken = auth.authToken();
            this.writer.setAuthToken(auth.authToken());
            return true;
        } catch (ResponseException e) {

            return false;
        }
    }

    public boolean register(String username, String password, String email)  {
        UserData user = new UserData(username, password, email);
        try{
            AuthData auth =  this.writer.makeRequest("POST", "/user", user, AuthData.class);
            this.authToken = auth.authToken();
            this.writer.setAuthToken(auth.authToken());
            return true;
        }catch(ResponseException e){
            return false;
        }



    }

    public boolean logout()  {
        if (this.authToken != null) {
            try{
                this.writer.makeRequest("DELETE", "/session", this.authToken, null);
                this.authToken = null;
                return true;
            } catch (ResponseException e) {
                return false;
            }
        }
        return false;
    }

    public ArrayList<GameData> listGames(){
        var games = new ArrayList<GameData>();
        try{
            games = this.writer.makeRequest("GET", "/game", null, GameList.class).games();
        }catch(ResponseException e){
            System.out.println("Error: " + e.getMessage());
            return new ArrayList<GameData>();
        }
        return games;
    }

    public boolean createGame(String title){
        try{
            GameData game = new GameData(title);
            this.writer.makeRequest("POST", "/game", game, GameData.class);
            return true;
        }catch(ResponseException e){
            return false;
        }

    }

    public boolean joinGame(String playerColor, int gameId) {
        Map req;

        req = Map.of("playerColor", playerColor, "gameID", gameId);
        try{
            this.writer.makeRequest("PUT", "/game", req, null);
            return true;
        }catch(ResponseException e){

            return false;
        }

    }
    
}
