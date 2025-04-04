package facade;


//import exception.ErrorResponse;
import exception.ResponseException;
import model.*;
import model.GameList;

        import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class ServerFacade {

    public String serverUrl;
    public String authToken = "";
    Writer writer;
    public ServerFacade() {
        this("localhost:8080");
    }
    public ServerFacade(String serverURL) {

        this.serverUrl = "http://" + serverURL;
        this.writer =  new Writer(this.authToken,this.serverUrl);
    }


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
        if (Objects.isNull(playerColor)){
            return true;
        }
        req = Map.of("playerColor", playerColor, "gameID", gameId);
        try{
            this.writer.makeRequest("PUT", "/game", req, null);
            // add websocket writing here
            return true;
        }catch(ResponseException e){

            return false;
        }

    }

}
