package manfred.game.attack;

import manfred.game.characters.Direction;
import manfred.game.characters.SkillSet;
import manfred.game.config.GameConfig;
import manfred.game.map.Map;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Stack;

@Component
public class CastModeOn implements CastMode {
    private final SkillSet skillSet;
    private final AttacksContainer attacksContainer;
    private final GameConfig gameConfig;
    private final BufferedImage castModeSprite;
    private Stack<CombinationElement> attackCombination = new Stack<>();

    public CastModeOn(SkillSet skillSet, AttacksContainer attacksContainer, GameConfig gameConfig, BufferedImage castModeSprite) {
        this.skillSet = skillSet;
        this.attacksContainer = attacksContainer;
        this.gameConfig = gameConfig;
        this.castModeSprite = castModeSprite;
    }

    @Override
    public CastMode cast(Map.Coordinate castCoordinate, Direction viewDirection) {
        this.skillSet.get(attackCombination)
            .map(attackGenerator -> attackGenerator.generate(castCoordinate, viewDirection))
            .map(attacksContainer::add);

        return off();
    }

    @Override
    public CastMode off() {
        attackCombination = new Stack<>();
        return new CastModeOff(this);
    }

    @Override
    public void addToCombination(CombinationElement combinationElement) {
        attackCombination.push(combinationElement);
    }

    @Override
    public void paint(Graphics g, Integer x, Integer y) {
        // TODO
        g.drawImage(
            castModeSprite,
            x - gameConfig.getPixelBlockSize().divideBy(2),
            y - gameConfig.getPixelBlockSize().divideBy(2),
            gameConfig.getPixelBlockSize().times(2).value(),
            gameConfig.getPixelBlockSize().times(3).value(),
            null
        );
    }
}
