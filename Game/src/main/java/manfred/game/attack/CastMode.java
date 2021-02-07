package manfred.game.attack;

import manfred.game.characters.Direction;
import manfred.game.map.Map;

import java.awt.*;

public interface CastMode {

    CastMode cast(Map.Coordinate castCoordinate, Direction viewDirection);

    CastMode off();

    void addToCombination(CombinationElement combinationElement);

    void paint(Graphics g, Integer x, Integer y);
}
