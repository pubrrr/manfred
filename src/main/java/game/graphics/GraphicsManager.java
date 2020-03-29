package game.graphics;

import game.exception.InvalidInputException;
import game.map.Map;
import game.map.MapReader;

import javax.swing.*;
import java.io.IOException;

public class GraphicsManager {
    private MapReader mapReader;
    private JFrame window;

    public GraphicsManager(ManfredFrame frame, MapReader mapReader) {
        this.window = frame;
        this.mapReader = mapReader;
    }

    public void loadMap(String name) {
        try {
            Map map = mapReader.convert(mapReader.read(MapReader.PATH_MAPS + name + ".json"));
            window.add(new GamePanel(map));
            window.pack();
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            String message = "Could not read map " + name + ": " + e.getMessage();
            System.out.println(message);
            e.printStackTrace();
        }
    }

    public void paint() {
        window.repaint();
    }
}