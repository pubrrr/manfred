package game;

import game.graphics.GraphicsManager;
import game.map.Map;
import game.map.MapReader;

public class Game {
    public static final String PATH_DATA = "data\\";
    public static final String PATH_MAPS = PATH_DATA + "maps\\";

    private static GraphicsManager graphics;

    public static void main(String[] args) {
        MapReader mapReader = new MapReader();
        try {
            Map map = mapReader.convert(mapReader.read(PATH_MAPS + "wald.json"));

            graphics = new GraphicsManager();
            graphics.init(map);
            graphics.paint();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
