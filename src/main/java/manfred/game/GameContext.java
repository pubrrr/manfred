package manfred.game;

import manfred.game.characters.Manfred;
import manfred.game.characters.MapCollider;
import manfred.game.controls.DoNothingController;
import manfred.game.controls.GelaberController;
import manfred.game.controls.KeyControls;
import manfred.game.controls.ManfredController;
import manfred.game.enemy.EnemyReader;
import manfred.game.enemy.MapColliderProvider;
import manfred.game.graphics.GamePanel;
import manfred.game.graphics.ManfredWindow;
import manfred.game.interact.PersonReader;
import manfred.game.interact.gelaber.Gelaber;
import manfred.game.interact.gelaber.GelaberReader;
import manfred.game.map.MapReader;
import manfred.game.map.MapWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GameContext {
    @Bean
    public Game game(GameRunner gameRunner) {
        Thread graphicsPainterThread = new Thread(gameRunner, "gameRunner");
        graphicsPainterThread.setDaemon(true);
        return new Game(graphicsPainterThread);
    }

    @Bean
    public GameRunner gameRunner(KeyControls keyControls, ManfredWindow window, Manfred manfred) {
        return new GameRunner(keyControls, window, manfred);
    }

    @Bean
    public Manfred manfred(MapCollider collider, MapWrapper mapWrapper) {
        return new Manfred(10, GamePanel.PIXEL_BLOCK_SIZE * 3, GamePanel.PIXEL_BLOCK_SIZE * 3, 100, collider, mapWrapper);
    }

    @Bean
    public GamePanel gamePanel(MapWrapper map, Manfred manfred) {
        return new GamePanel(map, manfred);
    }

    @Bean
    public ManfredWindow manfredWindow(GamePanel panel) {
        return new ManfredWindow(panel);
    }

    @Bean
    public KeyControls keyControls(
            ManfredController manfredController,
            GelaberController gelaberController,
            DoNothingController doNothingController,
            Manfred manfred,
            GamePanel panel,
            MapWrapper mapWrapper
    ) {
        KeyControls keyControls = new KeyControls(manfredController,
                gelaberController,
                doNothingController,
                manfred,
                panel,
                mapWrapper
        );
        panel.addKeyListener(keyControls);
        return keyControls;
    }

    @Bean
    public MapWrapper mapWrapper(MapReader mapReader) {
        return new MapWrapper(mapReader, "Wald");
    }

    @Bean
    public MapReader mapReader(PersonReader personReader, EnemyReader enemyReader) {
        return new MapReader(personReader, enemyReader);
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
        return new GelaberReader((Gelaber.TEXT_BOX_WIDTH - 2 * Gelaber.TEXT_DISTANCE_TO_BOX) / (Gelaber.TEXT_POINT_SIZE / 2));
    }

    @Bean
    public ManfredController manfredController(Manfred manfred) {
        return new ManfredController(manfred);
    }

    @Bean
    public GelaberController gelaberController() {
        return new GelaberController();
    }

    @Bean
    public DoNothingController doNothingController() {
        return new DoNothingController();
    }

    @Bean
    public EnemyReader enemyReader(MapColliderProvider mapColliderProvider) {
        return new EnemyReader(mapColliderProvider);
    }

    @Bean
    public MapColliderProvider mapColliderProvider() {
        return new MapColliderProvider();
    }
}
