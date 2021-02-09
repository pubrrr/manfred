package manfred.game.graphics.coordinatetransformation;

import lombok.AllArgsConstructor;
import manfred.data.shared.PositiveInt;
import manfred.game.graphics.PanelCoordinate;
import manfred.game.map.Map;

@AllArgsConstructor
public class MapCoordinateToPanelCoordinateTransformer {

    private final PositiveInt.Strict pixelBlockSize;

    public PanelCoordinate toPanelCoordinate(Map.Coordinate mapCoordinate) {
        return mapCoordinate.scaleTo(pixelBlockSize);
    }
}
