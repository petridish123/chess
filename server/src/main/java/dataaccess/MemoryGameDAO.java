package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashSet;


public class MemoryGameDAO implements GameDataAccess{
    public ArrayList<GameData> database;

    public MemoryGameDAO() {
        database = new ArrayList<GameData>();
    }

    @Override
    public ArrayList<GameData> listGames() {
        return database;
    }

    @Override
    public GameData getGame(int id) throws DataAccessException {
        for (GameData gameData : database) {
            if (gameData.gameID() == id) {
                return gameData;
            }
        }
        throw new DataAccessException("Game does not exist");
    }

    @Override
    public boolean getGameByname(String name) throws DataAccessException{
        for (GameData gameData : database) {
            if (gameData.gameName().equals(name)) {

                return true;
            }
        }

        return false;
    }

    @Override
    public void createGame(GameData game) throws DataAccessException{
        try{
            this.getGame(game.gameID());
            throw new DataAccessException("Game already exists");
        }
        catch(DataAccessException e){
            database.add(game);
        }
    }

    @Override
    public void updateGame(GameData game)  {
        try {
            database.remove(this.getGame(game.gameID()));
            database.add(game);
        }
        catch (DataAccessException e) {
            database.add(game);
        }
    }


    @Override
    public void clear(){
        database.clear();
    }
}
