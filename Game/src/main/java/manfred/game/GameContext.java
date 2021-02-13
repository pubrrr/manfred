package manfred.game;

import manfred.data.DataContext;
import manfred.data.InvalidInputException;
import manfred.data.persistence.reader.ConfigProvider;
import manfred.data.shared.PositiveInt;
import manfred.game.attack.AttacksContainer;
import manfred.game.attack.CastModeOn;
import manfred.game.attack.CombinationElement;
import manfred.game.characters.Manfred;
import manfred.game.characters.ManfredFramesLoader;
import manfred.game.characters.SkillSet;
import manfred.game.characters.Velocity;
import manfred.game.characters.sprite.DirectionalAnimatedSprite;
import manfred.game.config.GameConfig;
import manfred.game.controls.KeyControls;
import manfred.game.controls.ManfredController;
import manfred.game.graphics.scrolling.BackgroundScroller;
import manfred.game.graphics.GamePanel;
import manfred.game.graphics.coordinatetransformation.MapCoordinateToPanelCoordinateTransformer;
import manfred.game.interact.person.gelaber.LineSplitter;
import manfred.game.interact.person.textLineFactory.ChoicesTextLineFactory;
import manfred.game.interact.person.textLineFactory.SimpleTextLineFactory;
import manfred.game.interact.person.textLineFactory.TextLineFactory;
import manfred.game.map.MapFacade;
import manfred.infrastructureadapter.InfrastructureAdapterContext;
import manfred.infrastructureadapter.attack.AttackGeneratorProvider;
import manfred.infrastructureadapter.config.ConfigConverter;
import manfred.infrastructureadapter.map.MapProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Stack;

@Configuration
@ComponentScan(basePackages = "manfred.game")
@Import({DataContext.class, InfrastructureAdapterContext.class})
public class GameContext {
    @Bean
    public Game game(GameRunner gameRunner) {
        Thread graphicsPainterThread = new Thread(gameRunner, "gameRunner");
        graphicsPainterThread.setDaemon(true);
        return new Game(graphicsPainterThread);
    }

    @Bean
    public Manfred manfred(GameConfig gameConfig, ManfredFramesLoader manfredFramesLoader, MapFacade mapFacade) throws InvalidInputException {
        DirectionalAnimatedSprite sprite = new DirectionalAnimatedSprite(
            gameConfig.getPixelBlockSize(),
            gameConfig.getPixelBlockSize().times(2),
            manfredFramesLoader.loadWalkAnimation(),
            PositiveInt.of(4)
        );

        return new Manfred(
            Velocity.withSpeed(PositiveInt.of(6)),
            mapFacade.tileAt(PositiveInt.of(5), PositiveInt.of(27)).getBottomLeftCoordinate(),
            PositiveInt.of(gameConfig.getPixelBlockSize().value() - 2),
            PositiveInt.of(gameConfig.getPixelBlockSize().value() - 2),
            PositiveInt.of(100),
            sprite
        );
    }

    @Bean
    public CastModeOn castModeOn(SkillSet skillSet, AttacksContainer attacksContainer, GameConfig gameConfig, ManfredFramesLoader manfredFramesLoader) throws InvalidInputException {
        return new CastModeOn(skillSet, attacksContainer, gameConfig, manfredFramesLoader.loadCastModeSprite());
    }

    @Bean
    public KeyControls keyControls(ManfredController manfredController, GamePanel panel) {
        KeyControls keyControls = new KeyControls(manfredController);
        panel.addKeyListener(keyControls);
        return keyControls;
    }

    @Bean
    public MapFacade mapFacade(MapProvider mapProvider) throws InvalidInputException {
        return new MapFacade(mapProvider, mapProvider.provide("Wald"));
    }

    @Bean
    public SkillSet skillSet(AttackGeneratorProvider attackGeneratorProvider) throws Exception {
        SkillSet skillSet = new SkillSet();
        Stack<CombinationElement> combination = new Stack<>();
        combination.push(CombinationElement.LEFT);
        combination.push(CombinationElement.UP);
        combination.push(CombinationElement.RIGHT);
        combination.push(CombinationElement.UP);
        combination.push(CombinationElement.LEFT);
        skillSet.put(combination, attackGeneratorProvider.provide("thunder"));
        return skillSet;
    }

    @Bean
    public BackgroundScroller backgroundScroller(MapFacade mapFacade, GameConfig gameConfig, Manfred manfred, MapCoordinateToPanelCoordinateTransformer coordinateTransformer) {
        int triggerScrollDistanceToBorder = Math.min(gameConfig.getWindowHeight(), gameConfig.getWindowWidth()) / 3;
        return BackgroundScroller
            .factoryWith(triggerScrollDistanceToBorder, mapFacade, gameConfig)
            .buildCenteredAt(coordinateTransformer.toPanelCoordinate(manfred.getCenter()));
    }

    @Bean
    public GameConfig gameConfig(ConfigProvider configReader, ConfigConverter configConverter) throws InvalidInputException {
        return configConverter.convert(configReader.provide());
    }

    @Bean
    public LineSplitter lineSplitter(GameConfig gameConfig) {
        return new LineSplitter(gameConfig.getCharactersPerGelaberLine());
    }

    @Bean
    public TextLineFactory textLineFactory(GameConfig gameConfig) {
        return new TextLineFactory(
            ChoicesTextLineFactory.withConfig(gameConfig).orElse(SimpleTextLineFactory.withConfig(gameConfig))
        );
    }

    @Bean
    public MapCoordinateToPanelCoordinateTransformer mapCoordinateToPanelCoordinateTransformer(GameConfig gameConfig) {
        return new MapCoordinateToPanelCoordinateTransformer(gameConfig.getPixelBlockSize());
    }
}
