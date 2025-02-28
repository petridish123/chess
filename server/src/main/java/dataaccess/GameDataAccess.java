package dataaccess;
import dataaccess.DataAccessException;
import java.util.HashSet;
import model.*;
public interface GameDataAccess {

    public HashSet<GameData> listGames();
    public GameData getGame(int id) throws DataAccessException;
    public void createGame(GameData game) throws DataAccessException;
    public void updateGame(GameData game) ;
    public void deleteGame(int id);

}
