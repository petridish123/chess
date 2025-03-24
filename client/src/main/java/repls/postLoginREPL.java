package repls;

import server.ServerFacade;

import java.util.Scanner;

import static java.lang.System.out;

public class postLoginREPL {
    ServerFacade facade;
    public postLoginREPL(ServerFacade facade) {
        this.facade = facade;
    }

    void run(){
        var joined = false;
        while(!joined){
            String[] input = getUserInput();
            
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
