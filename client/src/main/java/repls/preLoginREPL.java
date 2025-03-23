package repls;

import exception.ResponseException;
import server.ServerFacade;

import java.util.Scanner;
import static ui.EscapeSequences.*;
import static java.lang.System.out;

public class preLoginREPL {
    ServerFacade facade;
    postLoginREPL postLogin;
    public preLoginREPL(ServerFacade facade) {
        this.facade = facade;
        this.postLogin = new postLoginREPL(facade);
    }

    public void run() {
        out.println(SET_BG_COLOR_RED + "Welcome to chess! \nPlease read the help to log in and get playing!" + RESET_BG_COLOR);
        helpPrint();
        boolean login = false;
        while (!login) {
            String[] input = getUserInput();
            switch (input[0]) {
                case "register":
                    if (input.length <4){
                        out.println("You didn't enter enough arguments!");
                        registerPrint();
                        break;
                    }
                    try{
                        facade.register(input[1], input[2], input[3]);
                        login = true;
                        break;
                    }catch (ResponseException e){
                        out.println("User already exists!");
                    }

                case "login":
                    if (input.length < 3) {
                        out.println("You didn't enter enough arguments!");
                        loginPrint();
                        break;
                    }
                    try {
                        facade.login(input[1],input[2]);
                        login = true;
                        break;
                    } catch (ResponseException e) {
                        out.println("Please use a valid username and password!");
                        loginPrint();
                    }
                    break;
                case "quit":
                    out.println("Goodbye!");
                    return;
                default:
                    helpPrint();
                    break;
            }
        }
        out.println();
        postLogin.run();
    }



    private String[] getUserInput() {
        out.print("\n[LOGGED OUT] >>> ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().split(" ");
    }

    private void helpPrint() {
        registerPrint();
        loginPrint();
        out.println("quit : exit");
        out.println("help : display help");
    }

    private void registerPrint() {
        out.println("register <USERNAME> <PASSWORD> <EMAIL> - create a new user");
    }

    private void loginPrint() {
        out.println("login <USERNAME> <PASSWORD> - login to an existing user");
    }
}
