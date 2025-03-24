package server;


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
    public String authToken;
    public ServerFacade() {
        this("localhost:8080");
    }
    public ServerFacade(String serverURL) {
        this.serverUrl = "http://" + serverURL;
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
            AuthData auth = makeRequest("POST", "/session", user, AuthData.class);
            this.authToken = auth.authToken();
            return true;
        } catch (ResponseException e) {

            return false;
        }
    }

    public boolean register(String username, String password, String email)  {
        UserData user = new UserData(username, password, email);
        try{
            AuthData auth =  makeRequest("POST", "/user", user, AuthData.class);
            this.authToken = auth.authToken();
            return true;
        }catch(ResponseException e){
            return false;
        }



    }

    public boolean logout()  {
        if (this.authToken != null) {
            try{
                this.makeRequest("DELETE", "/session", this.authToken, null);
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
            games = this.makeRequest("GET", "/game", null, GameList.class).games();
        }catch(ResponseException e){
            System.out.println("Error: " + e.getMessage());
            return new ArrayList<GameData>();
        }
        return games;
    }

    public boolean createGame(String title){
        try{
            GameData game = new GameData(title);
            this.makeRequest("POST", "/game", game, GameData.class);
            return true;
        }catch(ResponseException e){
            return false;
        }

    }

    public boolean joinGame(String playerColor, int gameId) {
        Map req;

        req = Map.of("playerColor", playerColor, "gameID", gameId);
        try{
            this.makeRequest("PUT", "/game", req, null);
            return true;
        }catch(ResponseException e){

            return false;
        }

    }

    public boolean clear(){
        try {
            this.makeRequest("DELETE", "/db", null, null);
            return true;
        }catch(ResponseException e){
            return false;
        }
    }








    /**
     *
     * @param method
     * @param path
     * @param request
     * @param responseClass
     * @return
     * @param <T>
     * @throws ResponseException
     */



    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {

            URL url = (new URI(serverUrl + path)).toURL();

            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {

            throw ex;
        } catch (Exception ex) {

            throw new ResponseException(500, ex.getMessage());
        }
    }


    private void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (this.authToken != null) {
            http.addRequestProperty("authorization", this.authToken);
        }
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");

            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }


    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
