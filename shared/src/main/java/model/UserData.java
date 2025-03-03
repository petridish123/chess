package model;

public record UserData (String username, String password, String email){

    UserData(String username, String password){
        this(username, password, "");
    }
}
