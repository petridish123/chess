package dataaccess;
import dataaccess.DataAccessException;

import java.util.Collection;
import java.util.HashSet;
import java.util.ArrayList;
import model.*;
public interface GameDataAccess {

    public ArrayList<GameData> listGames() throws DataAccessException;
    public GameData getGame(int id) throws DataAccessException;
    public void createGame(GameData game) throws DataAccessException;
    public void updateGame(GameData game) throws DataAccessException ;
    public void clear() throws DataAccessException;
    public boolean getGameByname(String name) throws DataAccessException;
}
