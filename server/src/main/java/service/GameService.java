package service;

import model.*;
import dataaccess.*;

/*
 *  clear
 *  add game
 *  list games
 *  join game
 *
 */



public class GameService {

    GameDataAccess gameDAO;

    public GameService(GameDataAccess gameDAO){
        this.gameDAO = gameDAO;
    }

    
}
