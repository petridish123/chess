package server;


import com.google.gson.JsonSyntaxException;
import model.AuthData;
import model.UserData;
import service.UserService;
import dataaccess.DataAccessException;
import spark.Response;
import spark.Request;
import com.google.gson.Gson;

import java.util.Objects;

/*
Register
Login
Logout
sir a second hypoxia has hit the bathrooms 💕💖❤
 */
public class UserHandler {

    UserService userService;
    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public Object register(Request req, Response res) {
        UserData user = new Gson().fromJson(req.body(), UserData.class);
        if (user == null||Objects.equals(user.username(),null)||Objects.equals(user.password(),null)){
            res.status(400);
            return "{\"message\": \"Error: Bad Request\"}";
        }
        AuthData authData;
        try {
            authData = userService.registerUser(user);
        }
        catch(DataAccessException e){
            res.status(403);
            return "{\"message\": \"Error: already taken\"}";
        }catch (Exception e){
            res.status(500);
            return "{\"message\": \"Error: " + e.getMessage() + "\"}";
        }


        res.status(200);
        return new Gson().toJson(authData);
    }


    public Object login(Request req, Response res) {
        UserData user = new Gson().fromJson(req.body(), UserData.class);
        if (Objects.equals(user.username(),null) || user.username().isEmpty() || Objects.equals(user.password(),null) ||user.password().isEmpty()) {
            res.status(400);
            return "{\"message\": \"Error: Bad Request\"}";
        }
        AuthData authData;
        try{
            authData = userService.loginUser(user);
        }catch(DataAccessException e){
            res.status(401);
            return "{\"message\": \"Error: unauthorized\"}";
        } catch (Exception e) {
            res.status(500);
            return "{\"message\": \"Error: " + e.getMessage() + "\"}";
        }

        res.status(200);
        return new Gson().toJson(authData);
    }

    public Object logout(Request req, Response res) {
        String authData;
        try {
            authData = new Gson().fromJson(req.headers("authorization"), String.class);
        } catch (JsonSyntaxException e) {
            res.status(400);
            return "{\"message\": \"Error: No Authorization\"}";
        }
        try{
            userService.logoutUser(authData);
        }catch (DataAccessException e){
            res.status(401);
            return "{\"message\": \"Error: unauthorized\"}";
        }catch (Exception e){
            res.status(500);
            return "{\"message\": \"Error: " + e.getMessage() + "\"}";
        }

        res.status(200);
        return "{}";
    }
}
