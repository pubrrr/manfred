package manfred.manfreditor.gui.view;

import manfred.data.shared.PositiveInt;
import manfred.manfreditor.map.Map;
import org.springframework.stereotype.Component;

@Component
public class ViewCoordinateMapper {
    public MapViewCoordinate mapToBottomLeft(Map.TileCoordinate tileCoordinate, PositiveInt.Strict sizeY) {
        int invertedY = sizeY.value() - tileCoordinate.getY().value();

        return new MapViewCoordinate(
            tileCoordinate.getX().times(TileViewSize.TILE_SIZE).value(),
            invertedY * TileViewSize.TILE_SIZE - 1
        );
    }
}
