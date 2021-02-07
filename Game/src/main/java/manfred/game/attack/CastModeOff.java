package manfred.game.attack;

import manfred.game.characters.Direction;
import manfred.game.map.Map;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class CastModeOff implements CastMode {
    private final CastModeOn castModeOn;

    public CastModeOff(CastModeOn castModeOn) {
        this.castModeOn = castModeOn;
    }

    @Override
    public CastMode cast(Map.Coordinate castCoordinate, Direction viewDirection) {
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

    public void paint(Graphics g, Integer x, Integer y) {
        //paint nothing
    }
}
