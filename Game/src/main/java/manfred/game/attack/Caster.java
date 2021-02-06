package manfred.game.attack;

import manfred.game.characters.Direction;
import manfred.game.graphics.paintable.LocatedPaintable;
import manfred.game.map.Map;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class Caster implements LocatedPaintable {
    private CastMode castMode;

    public Caster(CastModeOff initialCastModeOff) {
        this.castMode = initialCastModeOff;
    }

    public void cast(Map.Coordinate castCoordinate, Direction viewDirection) {
        this.castMode = this.castMode.cast(castCoordinate, viewDirection);
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
