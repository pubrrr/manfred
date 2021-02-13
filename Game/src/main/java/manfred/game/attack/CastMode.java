package manfred.game.attack;

import manfred.game.characters.Direction;
import manfred.game.graphics.GraphicsAdapter;
import manfred.game.graphics.PanelCoordinate;
import manfred.game.map.Map;

public interface CastMode {

    CastMode cast(Map.Coordinate castCoordinate, Direction viewDirection);

    CastMode off();

    void addToCombination(CombinationElement combinationElement);

    void paint(GraphicsAdapter g, PanelCoordinate bottomLeftCoordinate);
}
