package manfred.game.attack;

import manfred.game.characters.Direction;
import manfred.game.graphics.GraphicsAdapter;
import manfred.game.graphics.PanelCoordinate;
import manfred.game.graphics.paintable.LocatedPaintable;
import manfred.game.map.Map;
import org.springframework.stereotype.Component;

@Component
public class Caster implements LocatedPaintable {
    private CastMode castMode;

    public Caster(CastModeOff initialCastModeOff) {
        this.castMode = initialCastModeOff;
    }

    public void cast(Map.Coordinate manfredCenterCoordinate, Direction viewDirection) {
        this.castMode = this.castMode.cast(manfredCenterCoordinate, viewDirection);
    }

    public void addToCombination(CombinationElement combinationElement) {
        this.castMode.addToCombination(combinationElement);
    }

    @Override
    public void paint(GraphicsAdapter g, PanelCoordinate bottomLeftCoordinate) {
        castMode.paint(g, bottomLeftCoordinate);
    }

    public void off() {
        this.castMode = this.castMode.off();
    }
}
