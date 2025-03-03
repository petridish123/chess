package server;
import model.GameData;

import java.util.Map;
import java.util.HashSet;
import java.util.ArrayList;
public record GameList(HashSet<GameData> games) {

}
