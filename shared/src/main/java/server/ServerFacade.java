package server;


import com.google.gson.Gson;
//import exception.ErrorResponse;
import exception.ResponseException;
import model.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ServerFacade {

    public String serverUrl;
    public String authToken;

    public ServerFacade(String serverURL) {
        this.serverUrl = serverURL;
    }

    /**
     * NOTE for any function that takes in multiple, you need to turn it into a map
     * TODONE : Create a Login function || takes in a userData and then returns an authData object
     * TODONE : Create a register || Takes in a user data and returns an authData object
     * TODONE : Create a logout || takes in a authtoken string? and returns VOID
     * TODONE : Create a list games || takes in a string authToken and returns a list of gameData
     * TODONE : create a create game || takes in a string auth and a string gameName and returns a game ID
     * TODO : create  join game ( called play game) || takes in an authToken, player color and gameID
     * TODO : create a clear function (low priority) || NOTHING RAHHHH
     *
     */
    public AuthData login(String username, String password) throws ResponseException {
        UserData user = new UserData(username, password);
        AuthData auth =  makeRequest("POST", "/session", user, AuthData.class);
        this.authToken = auth.authToken();
        return auth;
    }

    public AuthData register(String username, String password, String email) throws ResponseException {
        UserData user = new UserData(username, password, email);
        AuthData auth =  makeRequest("POST", "/user", user, AuthData.class);
        this.authToken = auth.authToken();
        return auth;
    }

    public void logout() throws ResponseException {
        if (this.authToken != null) {
            this.makeRequest("DELETE", "/session", this.authToken, null);
            this.authToken = null;
        }
    }

    public ArrayList listGames() throws ResponseException {
        return this.makeRequest("GET", "/game", this.authToken, ArrayList.class);
    }

    public GameData createGame(String title) throws ResponseException {
        GameData game = new GameData(title);
        return this.makeRequest("POST", "/game", game, GameData.class);
    }

    public void joinGame(String playerColor, int gameId) throws ResponseException {

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


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
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
