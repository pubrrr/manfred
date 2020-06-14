package manfred.game;

import manfred.game.attack.AttackReader;
import manfred.game.attack.AttacksContainer;
import manfred.game.characters.Manfred;
import manfred.game.characters.MapCollider;
import manfred.game.characters.SkillSet;
import manfred.game.controls.DoNothingController;
import manfred.game.controls.GelaberController;
import manfred.game.controls.KeyControls;
import manfred.game.controls.ManfredController;
import manfred.game.enemy.EnemiesWrapper;
import manfred.game.enemy.EnemyReader;
import manfred.game.enemy.MapColliderProvider;
import manfred.game.exception.InvalidInputException;
import manfred.game.graphics.BackgroundScroller;
import manfred.game.graphics.GamePanel;
import manfred.game.graphics.ImageLoader;
import manfred.game.graphics.ManfredWindow;
import manfred.game.interact.PersonReader;
import manfred.game.interact.gelaber.GelaberReader;
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
    public GameRunner gameRunner(KeyControls keyControls, ManfredWindow window, Manfred manfred, EnemiesWrapper enemiesWrapper, AttacksContainer attacksContainer) {
        return new GameRunner(keyControls, window, manfred, enemiesWrapper, attacksContainer);
    }

    @Bean
    public Manfred manfred(MapCollider collider, MapWrapper mapWrapper, AttacksContainer attacksContainer, SkillSet skillSet, GameConfig gameConfig) {
        return new Manfred(
                10,
                gameConfig.getPixelBlockSize() * 3,
                gameConfig.getPixelBlockSize() * 3,
                100,
                collider,
                mapWrapper,
                attacksContainer,
                skillSet,
                gameConfig
        );
    }

    @Bean
    public GamePanel gamePanel(MapWrapper map, Manfred manfred, EnemiesWrapper enemiesWrapper, AttacksContainer attacksContainer, BackgroundScroller backgroundScroller, GameConfig gameConfig) {
        return new GamePanel(map, manfred, enemiesWrapper, attacksContainer, backgroundScroller, gameConfig);
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
            MapWrapper mapWrapper,
            GameConfig gameConfig
    ) {
        KeyControls keyControls = new KeyControls(manfredController,
                gelaberController,
                doNothingController,
                manfred,
                panel,
                mapWrapper,
                gameConfig
        );
        panel.addKeyListener(keyControls);
        return keyControls;
    }

    @Bean
    public MapWrapper mapWrapper(MapReader mapReader, AttacksContainer attacksContainer) {
        return new MapWrapper(mapReader, "Wald", attacksContainer);
    }

    @Bean
    public MapReader mapReader(PersonReader personReader, EnemyReader enemyReader, EnemiesWrapper enemiesWrapper, GameConfig gameConfig) {
        return new MapReader(personReader, enemyReader, enemiesWrapper, gameConfig);
    }

    @Bean
    public MapCollider mapCollider(MapWrapper mapWrapper, GameConfig gameConfig) {
        return new MapCollider(mapWrapper, gameConfig);
    }

    @Bean
    public PersonReader personReader(GelaberReader gelaberReader) {
        return new PersonReader(gelaberReader);
    }

    @Bean
    public GelaberReader gelaberReader(GameConfig gameConfig) {
        return new GelaberReader(gameConfig);
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
    public EnemyReader enemyReader(MapColliderProvider mapColliderProvider, ImageLoader imageLoader, GameConfig gameConfig) {
        return new EnemyReader(mapColliderProvider, imageLoader, gameConfig);
    }

    @Bean
    public MapColliderProvider mapColliderProvider() {
        return new MapColliderProvider();
    }

    @Bean
    public EnemiesWrapper enemiesWrapper() {
        return new EnemiesWrapper();
    }

    @Bean
    public AttacksContainer attacksContainer() {
        return new AttacksContainer();
    }

    @Bean
    public SkillSet skillSet(AttackReader attackReader) throws InvalidInputException, IOException {
        SkillSet skillSet = new SkillSet();
        skillSet.put("lurul", attackReader.load("throwBlock"));
        return skillSet;
    }

    @Bean
    public AttackReader attackReader(MapColliderProvider mapColliderProvider) {
        return new AttackReader(mapColliderProvider);
    }

    @Bean
    public BackgroundScroller backgroundScroller(Manfred manfred, MapWrapper mapWrapper, GameConfig gameConfig) {
        int triggerScrollDistanceToBorder = Math.min(gameConfig.getWindowHeight(), gameConfig.getWindowWidth()) / 3;
        return new BackgroundScroller(triggerScrollDistanceToBorder, manfred, mapWrapper, gameConfig);
    }

    @Bean
    public ImageLoader imageLoader() {
        return new ImageLoader();
    }

    @Bean
    public GameConfig gameConfig(ConfigReader configReader) throws InvalidInputException, IOException {
        return configReader.load();
    }

    @Bean
    public ConfigReader configReader() {
        return new ConfigReader();
    }
}
