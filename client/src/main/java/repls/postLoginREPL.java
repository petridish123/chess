package repls;

import model.GameData;
import server.ServerFacade;

import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.System.out;

public class postLoginREPL {
    ServerFacade facade;
    preLoginREPL preREPL;
    public postLoginREPL(ServerFacade facade, preLoginREPL preREPL) {
        this.facade = facade;
        this.preREPL = preREPL;
    }

    void run(){
        var joined = false;
        while(!joined){
            String[] input = getUserInput();
            switch (input[0]) {
                case "logout":
                    if (facade.logout()){
                        preREPL.run();
                        return;
                    }
                    else{
                        out.println("Something went wrong");
                    }

                case "join":
                    if (input.length < 3){
                        out.println("not enough arguments!");
                        joinPrint();
                        break;
                    }
                    else{
                        if (facade.joinGame(input[2], Integer.parseInt(input[1]))){
                            joined = true;
                            out.println("You joined the game as " + input[2]);
                        }
                        else{
                            out.println("Please type in a valid game number and color");
                            joinPrint();
                        }
                        break;
                    }


                case "create":
                    if (input.length < 2){
                        out.println("not enough arguments!");
                    }
                    else{
                        if (facade.createGame(input[1])){
                            out.println("You created the game as " + input[1]);
                        }
                        else{
                            out.println("Game already exists!");
                        }
                        break;
                    }
                case "list":
                    ArrayList<GameData> games = facade.listGames();
                    for (GameData game: games){
                        out.println(game);
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
    }

    private String[] getUserInput() {
        out.print("\n[LOGGED IN] >>> ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().split(" ");
    }

    private void helpPrint() {
        logoutPrint();
        joinPrint();
        createPrint();
        listPrint();
        out.println("quit : exit");
        out.println("help : display help");
    }

    private void listPrint() {
        out.println("list : list all games");
    }

    private void createPrint() {
        out.println("create <gamename>: create game with the given name");
    }

    private void joinPrint() {
        out.println("join <gamenumber> <WHITE|BLACK>: join game with the given name as the given player color");
    }

    private void logoutPrint() {
        out.println("logout : logout");
    }
}
