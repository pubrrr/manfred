package manfred.game.attack;

import manfred.game.characters.Direction;
import manfred.game.characters.Sprite;
import manfred.game.graphics.Paintable;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class Caster implements Paintable {
    private CastMode castMode;

    public Caster(CastModeOff initialCastModeOff) {
        this.castMode = initialCastModeOff;
    }

    public void cast(Sprite sprite, Direction viewDirection) {
        this.castMode = this.castMode.cast(sprite, viewDirection);
    }

    public void addToCombination(CombinationElement combinationElement) {
        this.castMode.addToCombination(combinationElement);
    }

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
        castMode.paint(g, offset, x, y);
    }

    public void off() {
        this.castMode = this.castMode.off();
    }
}
