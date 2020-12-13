package manfred.game;

import manfred.game.attack.AttackReader;
import manfred.game.attack.AttacksContainer;
import manfred.game.attack.CastModeOn;
import manfred.game.attack.CombinationElement;
import manfred.game.characters.Manfred;
import manfred.game.characters.ManfredFramesLoader;
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
import manfred.game.graphics.*;
import manfred.game.interact.PersonReader;
import manfred.game.interact.gelaber.GelaberReader;
import manfred.game.map.MapReader;
import manfred.game.map.MapWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Stack;

@Configuration
@ComponentScan
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
    public Manfred manfred(
        MapCollider collider,
        MapWrapper mapWrapper,
        GameConfig gameConfig,
        ManfredFramesLoader manfredFramesLoader
    ) throws IOException {
        return new Manfred(
            6,
            gameConfig.getPixelBlockSize() * 3,
            gameConfig.getPixelBlockSize() * 3,
            gameConfig.getPixelBlockSize(),
            2 * gameConfig.getPixelBlockSize(),
            100,
            collider,
            mapWrapper,
            gameConfig,
            manfredFramesLoader.loadWalkAnimation()
        );
    }

    @Bean
    public CastModeOn castModeOn(SkillSet skillSet, AttacksContainer attacksContainer, GameConfig gameConfig, Manfred manfred, ManfredFramesLoader manfredFramesLoader) throws IOException {
        return new CastModeOn(skillSet, attacksContainer, gameConfig, manfred.getSprite(), manfredFramesLoader.loadCastModeSprite());
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
        GameConfig gameConfig,
        BackgroundScroller backgroundScroller
    ) {
        KeyControls keyControls = new KeyControls(manfredController,
            gelaberController,
            doNothingController,
            manfred,
            panel,
            mapWrapper,
            gameConfig,
            backgroundScroller
        );
        panel.addKeyListener(keyControls);
        return keyControls;
    }

    @Bean
    public MapWrapper mapWrapper(MapReader mapReader, AttacksContainer attacksContainer) {
        return new MapWrapper(mapReader, "Wald", attacksContainer);
    }

    @Bean
    public MapReader mapReader(PersonReader personReader, EnemyReader enemyReader, EnemiesWrapper enemiesWrapper, GameConfig gameConfig, ImageLoader imageLoader) {
        return new MapReader(personReader, enemyReader, enemiesWrapper, gameConfig, imageLoader);
    }

    @Bean
    public MapCollider mapCollider(MapWrapper mapWrapper, GameConfig gameConfig) {
        return new MapCollider(mapWrapper, gameConfig);
    }

    @Bean
    public PersonReader personReader(GelaberReader gelaberReader, GameConfig gameConfig, ImageLoader imageLoader) {
        return new PersonReader(gelaberReader, gameConfig, imageLoader);
    }

    @Bean
    public GelaberReader gelaberReader(GameConfig gameConfig) {
        return new GelaberReader(gameConfig);
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
    public SkillSet skillSet(AttackReader attackReader, MapCollider mapCollider) throws InvalidInputException, IOException {
        //refactor this! the MapCollider is here because it needs to be constructed first.

        SkillSet skillSet = new SkillSet();
        Stack<CombinationElement> combination = new Stack<>();
        combination.push(CombinationElement.LEFT);
        combination.push(CombinationElement.UP);
        combination.push(CombinationElement.RIGHT);
        combination.push(CombinationElement.UP);
        combination.push(CombinationElement.LEFT);
        skillSet.put(combination, attackReader.load("thunder"));
        return skillSet;
    }

    @Bean
    public AttackReader attackReader(MapColliderProvider mapColliderProvider, ImageLoader imageLoader) {
        return new AttackReader(mapColliderProvider, imageLoader);
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

    @Bean
    public ManfredFramesLoader manfredFramesLoader(ImageLoader imageLoader) {
        return new ManfredFramesLoader(imageLoader);
    }

    @Bean
    public PaintablesSorter paintablesSorter() {
        return new PaintablesSorter();
    }
}
