package manfred.game.graphics.coordinatetransformation;

import manfred.game.graphics.PanelCoordinate;
import manfred.game.map.Map;

@FunctionalInterface
public interface MapCoordinateToPanelCoordinateTransformer {
    PanelCoordinate toPanelCoordinate(Map.Coordinate mapCoordinate);
}
