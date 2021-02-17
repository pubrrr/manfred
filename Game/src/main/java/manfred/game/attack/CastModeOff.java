package manfred.game.attack;

import manfred.game.characters.Direction;
import manfred.game.graphics.GraphicsAdapter;
import manfred.game.graphics.PanelCoordinate;
import manfred.game.map.Map;
import org.springframework.stereotype.Component;

@Component
public class CastModeOff implements CastMode {
    private final CastModeOn castModeOn;

    public CastModeOff(CastModeOn castModeOn) {
        this.castModeOn = castModeOn;
    }

    @Override
    public CastMode cast(Map.Coordinate manfredCenterCoordinate, Direction viewDirection) {
        return this.castModeOn;
    }

    @Override
    public CastMode off() {
        return this;
    }

    @Override
    public void addToCombination(CombinationElement combinationElement) {
        //do nothing
    }

    public void paint(GraphicsAdapter g, PanelCoordinate bottomLeftCoordinate) {
        //paint nothing
    }
}
