package facade;


//import exception.ErrorResponse;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import exception.ResponseException;
import model.*;
import model.GameList;
import websocket.commands.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class ServerFacade {

    public String serverUrl;
    public String authToken = "";
    Writer writer;
    WebsocketWriter ws;
    String serverDomain;
    String username;
    public ServerFacade() {
        this("localhost:8080");
    }
    public ServerFacade(String serverURL) {

        this.serverUrl = "http://" + serverURL;
        this.writer =  new Writer(this.authToken,this.serverUrl);
        this.serverDomain = serverURL;
    }


    public boolean login(String username, String password) {
        UserData user = new UserData(username, password);
        try {
            AuthData auth = this.writer.makeRequest("POST", "/session", user, AuthData.class);
            this.authToken = auth.authToken();
            this.writer.setAuthToken(auth.authToken());
            this.username = auth.username();
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
            this.username = auth.username();
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

    public boolean joinGame(String playerColor, int gameId) throws ResponseException {
        try{
            this.ws = new WebsocketWriter(this.serverDomain);
        }catch(Exception e){
            System.out.println("Failed to connect to server");
            return false;
        }
        Map req;
        try{
        if (Objects.equals(playerColor, null)) {
//            req = Map.of("playerColor", null, "gameID", gameId);
//            this.writer.makeRequest("PUT", "/game", req, null);
            sendCommand(new Connect(this.authToken,gameId, null,this.username));


            return true;
        }} catch (Exception e){
            return false;
        }

        req = Map.of("playerColor", playerColor, "gameID", gameId);
        ChessGame.TeamColor color = playerColor.equals("WHITE") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
        try{
            this.writer.makeRequest("PUT", "/game", req, null);
            sendCommand(new Connect(this.authToken,gameId, color,this.username));
            // add websocket writing here
            return true;
        }catch(ResponseException e){

            return false;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }

    }

    public void leave(int gameID, ChessGame.TeamColor color) {
        sendCommand(new Leave(authToken,gameID));
    }


    public void resign(int gameID) {
        sendCommand(new Resign(authToken, gameID));
    }

    public boolean makeMove(int gameId, String position1, String position2, ChessPiece.PieceType promotion){
        ChessPosition initialPosition = parseMove(position1);
        ChessPosition finalPosition = parseMove(position2);
        if (Objects.equals(initialPosition, null) ||  Objects.equals(finalPosition,null) || initialPosition.equals(finalPosition) ){
//            System.out.println("Move BRICKED");
            return false;
        }
        sendCommand(new MakeMove(authToken,gameId, new ChessMove(initialPosition, finalPosition, promotion))); // change this
//        System.out.println("Move sucks");
        return true;
    }



    ChessPosition parseMove(String move){

        try{
            char moveChar = move.toUpperCase().charAt(0);
            int moveNum = Integer.parseInt(move.substring(1));
            int col = moveChar + 1 - 'A'; // maybe wrong
//            System.out.println("HERE LIES: " + String.valueOf(moveNum) + " " + String.valueOf(col));
            return new ChessPosition(moveNum,col);
        }catch(NumberFormatException e){
            return null;
        }

    }

    public void sendCommand(UserGameCommand command) {
        String message = new Gson().toJson(command);
        ws.sendMessage(message);
    }



}
