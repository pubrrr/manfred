package manfred.game;

import manfred.game.controls.KeyControls;
import manfred.game.exception.InvalidInputException;
import manfred.game.graphics.GamePanel;
import manfred.game.graphics.ManfredWindow;
import manfred.game.map.Map;
import manfred.game.map.MapReader;
import manfred.game.map.MapWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GameContext {
    @Bean
    public Game game(GameRunner gameRunner) {
        Thread graphicsPainterThread = new Thread(gameRunner, "gameRunner");
        graphicsPainterThread.setDaemon(true);
        return new Game(graphicsPainterThread);
    }

    @Bean
    public GameRunner gameRunner(ManfredWindow window, Manfred manfred) {
        return new GameRunner(window, manfred);
    }

    @Bean
    public Manfred manfred() {
        return new Manfred(GamePanel.PIXEL_BLOCK_SIZE * 3, GamePanel.PIXEL_BLOCK_SIZE * 3);
    }

    @Bean
    public GamePanel gamePanel(Map map, Manfred manfred, KeyControls keyControls) {
        return new GamePanel(map, manfred, keyControls);
    }

    @Bean
    public ManfredWindow manfredWindow(GamePanel panel) {
        return new ManfredWindow(panel);
    }

    @Bean
    public KeyControls keyControls(Manfred manfred) {
        return new KeyControls(manfred);
    }

    @Bean
    public MapWrapper mapWrapper(MapReader mapReader, Map initialMap) {
        return new MapWrapper(mapReader, initialMap);
    }

    @Bean
    public MapReader mapReader() {
        return new MapReader();
    }

    @Bean
    public Map map(MapReader mapReader) throws InvalidInputException, IOException {
        return mapReader.loadMap("Wald");
    }
}
