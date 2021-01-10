package manfred.game.attack;

import manfred.game.config.GameConfig;
import manfred.game.characters.Direction;
import manfred.game.characters.SkillSet;
import manfred.game.characters.Sprite;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Stack;

@Component
public class CastModeOn implements CastMode {
    private final SkillSet skillSet;
    private final AttacksContainer attacksContainer;
    private final GameConfig gameConfig;
    private final Sprite manfredSprite;
    private final BufferedImage castModeSprite;
    private Stack<CombinationElement> attackCombination = new Stack<>();

    public CastModeOn(SkillSet skillSet, AttacksContainer attacksContainer, GameConfig gameConfig, Sprite manfredSprite, BufferedImage castModeSprite) {
        this.skillSet = skillSet;
        this.attacksContainer = attacksContainer;
        this.gameConfig = gameConfig;
        this.manfredSprite = manfredSprite;
        this.castModeSprite = castModeSprite;
    }

    @Override
    public CastMode cast(Sprite sprite, Direction viewDirection) {
        this.skillSet.get(attackCombination)
            .map(attackGenerator -> attackGenerator.generate(sprite.getCenter(), viewDirection))
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
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
        g.drawImage(
            castModeSprite,
            manfredSprite.x - gameConfig.getPixelBlockSize() / 2 - offset.x,
            manfredSprite.y - gameConfig.getPixelBlockSize() / 2 - offset.y,
            manfredSprite.width + gameConfig.getPixelBlockSize(),
            manfredSprite.height + gameConfig.getPixelBlockSize(),
            null
        );
    }
}
