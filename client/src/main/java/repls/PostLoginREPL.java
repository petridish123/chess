package repls;

import model.GameData;
import facade.ServerFacade;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.System.out;

public class PostLoginREPL {
    ServerFacade facade;
    PreLoginREPL preREPL;
    GameREPL gameREPL;
    ArrayList<GameData> games = new ArrayList<>();
    public PostLoginREPL(ServerFacade facade, PreLoginREPL preREPL) {
        this.facade = facade;
        this.preREPL = preREPL;
        gameREPL = new GameREPL(facade, this);
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

                        try {
                            int id = Integer.parseInt(input[1]);
                            String color = input[2].toUpperCase();
                            if (facade.joinGame(input[2], id+1)) {
                                getGames();
                                joined = true;
                                out.println("You joined the game as " + input[2]);
                                this.gameREPL.setGame(Integer.parseInt(input[1]),input[2], games.get(id));
                                this.gameREPL.run();
                                break;
                            }
                            else{
                                out.println("Please type in a valid game number and color");
                                joinPrint();
                            }
                        }catch (Exception e) {
                            out.println("Please type in a valid game number and color");
                            joinPrint();
                        }
                        break;


                case "observe":
                    if (input.length < 2){
                        out.println("not enough arguments!");
                    }
                    try {
                        int id = Integer.parseInt(input[1]);
                        if (facade.joinGame(null, id+1)) {
                            getGames();
                            joined = true;
                            out.println("You joined the game as observer");
                            this.gameREPL.setGame(Integer.parseInt(input[1]),null, games.get(id));

                            this.gameREPL.run();
                        }
                        else{
                            out.println("Please type in a valid gameID");

                        }
                    }catch (Exception e) {
                        out.println("Please type in a valid gameID");

                    }
                    break;

                case "create":
                    if (input.length < 2){
                        out.println("not enough arguments!");
                        break;
                    }

                        if (facade.createGame(input[1])){
                            out.println("You created the game as " + input[1]);
                        }
                        else{
                            out.println("Game already exists!");
                        }
                        break;

                case "list":
                    ArrayList<GameData> games = facade.listGames();
                    for (GameData game: games){
                        int id = game.gameID() - 1;
                        String name = game.gameName();
                        String white = (!Objects.equals(game.whiteUsername(), null)) ? game.whiteUsername()  : "Empty";
                        String black = (!Objects.equals(game.blackUsername(), null)) ? game.blackUsername()  : "Empty";
                        out.println("("+id+")" + "  name : " + name + "| white player : " + white + "| black player : " + black );
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

    private void getGames() {
        games = new ArrayList<>();
        ArrayList<GameData> gameList = facade.listGames();
        games.addAll(gameList);
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
