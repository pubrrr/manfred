package manfred.game;

import manfred.game.characters.Manfred;
import manfred.game.characters.MapCollider;
import manfred.game.controls.KeyControls;
import manfred.game.exception.InvalidInputException;
import manfred.game.graphics.GamePanel;
import manfred.game.graphics.ManfredWindow;
import manfred.game.interact.PersonReader;
import manfred.game.interact.gelaber.GelaberReader;
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
    public Manfred manfred(MapCollider collider) {
        return new Manfred(GamePanel.PIXEL_BLOCK_SIZE * 3, GamePanel.PIXEL_BLOCK_SIZE * 3, collider);
    }

    @Bean
    public GamePanel gamePanel(MapWrapper map, Manfred manfred, KeyControls keyControls) {
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
    public MapReader mapReader(PersonReader personReader) {
        return new MapReader(personReader);
    }

    @Bean
    public Map map(MapReader mapReader) throws InvalidInputException, IOException {
        return mapReader.load("Wald");
    }

    @Bean
    public MapCollider mapCollider(MapWrapper mapWrapper) {
        return new MapCollider(mapWrapper);
    }

    @Bean
    public PersonReader personReader(GelaberReader gelaberReader) {
        return new PersonReader(gelaberReader);
    }

    @Bean
    public GelaberReader gelaberReader() {
        return new GelaberReader();
    }
}
