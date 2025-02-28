package dataaccess;

import model.GameData;

import java.util.HashSet;


public class MemoryGameDAO implements GameDataAccess{
    public HashSet<GameData> database;

    public MemoryGameDAO() {
        database = new HashSet<GameData>();
    }

    @Override
    public HashSet<GameData> listGames() {
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
    public void deleteGame(int id) {
        database.removeIf(gameData -> gameData.gameID() == id);
    }

    public void clear(){
        database.clear();
    }
}
