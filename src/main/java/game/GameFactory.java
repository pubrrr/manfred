package game;

import game.controls.KeyControls;
import game.exception.InvalidInputException;
import game.graphics.GamePanel;
import game.graphics.GraphicsManager;
import game.graphics.ManfredWindow;
import game.map.Map;
import game.map.MapReader;

import java.io.IOException;

public class GameFactory {
    public static final String PATH_DATA = "data\\";

    public Game create() throws InvalidInputException, IOException {
        Manfred manfred = new Manfred(GamePanel.PIXEL_BLOCK_SIZE * 3, GamePanel.PIXEL_BLOCK_SIZE * 3);
        Map map = loadMap("Wald");
        KeyControls keyControls = new KeyControls(manfred);

        GraphicsManager graphics = new GraphicsManager();
        graphics.initGraphics(map, keyControls, new ManfredWindow(), manfred);

        Thread graphicsPainterThread = new Thread(new GameRunner(graphics, manfred), "gameRunner");
        graphicsPainterThread.setDaemon(true);
        return new Game(graphicsPainterThread);
    }

    private Map loadMap(String name) throws InvalidInputException, IOException {
        MapReader mapReader = new MapReader();
        String jsonMap = mapReader.read(MapReader.PATH_MAPS + name + ".json");
        return mapReader.convert(jsonMap);
    }
}
