package manfred.game.attack;

import manfred.game.characters.Direction;
import manfred.game.graphics.paintable.LocatedPaintable;
import manfred.game.map.Map;

public interface CastMode extends LocatedPaintable {

    CastMode cast(Map.Coordinate castCoordinate, Direction viewDirection);

    CastMode off();

    void addToCombination(CombinationElement combinationElement);
}
