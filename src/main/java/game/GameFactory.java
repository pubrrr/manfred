package game;

import game.graphics.GraphicsManager;
import game.graphics.GraphicsPainter;
import game.graphics.ManfredFrame;
import game.map.MapReader;

public class GameFactory {
    public static final String PATH_DATA = "data\\";

    public Game create() {
        GraphicsManager graphics = new GraphicsManager(new ManfredFrame(), new MapReader());
        graphics.loadMap("wald");

        Thread graphicsPainterThread = new Thread(new GraphicsPainter(graphics), "graphicsPainter");
        graphicsPainterThread.setDaemon(true);

        return new Game(graphicsPainterThread);
    }
}
