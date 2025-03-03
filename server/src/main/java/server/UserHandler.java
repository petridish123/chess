package server;


import dataaccess.UnAuthorizedException;
import model.AuthData;
import model.UserData;
import service.UserService;
import dataaccess.DataAccessException;
import model.GameData;
import spark.Response;
import spark.Request;
import com.google.gson.Gson;
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
        if (user.username().isEmpty() || user.password().isEmpty()) {
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
        if (user.username().isEmpty() || user.password().isEmpty()) {
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
        AuthData authData = new Gson().fromJson(req.headers("authorization"), AuthData.class);

        try{
            userService.logoutUser(authData.authToken());
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
