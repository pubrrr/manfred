package manfred.game;

import manfred.game.attack.AttackReader;
import manfred.game.attack.AttacksContainer;
import manfred.game.attack.CastModeOn;
import manfred.game.attack.CombinationElement;
import manfred.game.characters.Manfred;
import manfred.game.characters.ManfredFramesLoader;
import manfred.game.characters.MapCollider;
import manfred.game.characters.SkillSet;
import manfred.game.controls.KeyControls;
import manfred.game.controls.ManfredController;
import manfred.game.exception.InvalidInputException;
import manfred.game.graphics.BackgroundScroller;
import manfred.game.graphics.GamePanel;
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
    public Manfred manfred(
        MapCollider collider,
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
            gameConfig,
            manfredFramesLoader.loadWalkAnimation()
        );
    }

    @Bean
    public CastModeOn castModeOn(SkillSet skillSet, AttacksContainer attacksContainer, GameConfig gameConfig, Manfred manfred, ManfredFramesLoader manfredFramesLoader) throws IOException {
        return new CastModeOn(skillSet, attacksContainer, gameConfig, manfred.getSprite(), manfredFramesLoader.loadCastModeSprite());
    }

    @Bean
    public KeyControls keyControls(ManfredController manfredController, GamePanel panel) {
        KeyControls keyControls = new KeyControls(manfredController, panel);
        panel.addKeyListener(keyControls);
        return keyControls;
    }

    @Bean
    public MapWrapper mapWrapper(MapReader mapReader, AttacksContainer attacksContainer) {
        return new MapWrapper(mapReader, "Wald", attacksContainer);
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
    public BackgroundScroller backgroundScroller(Manfred manfred, MapWrapper mapWrapper, GameConfig gameConfig) {
        int triggerScrollDistanceToBorder = Math.min(gameConfig.getWindowHeight(), gameConfig.getWindowWidth()) / 3;
        return new BackgroundScroller(triggerScrollDistanceToBorder, manfred, mapWrapper, gameConfig);
    }

    @Bean
    public GameConfig gameConfig(ConfigReader configReader) throws InvalidInputException, IOException {
        return configReader.load();
    }
}
