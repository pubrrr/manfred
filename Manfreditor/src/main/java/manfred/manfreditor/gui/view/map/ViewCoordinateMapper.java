package manfred.manfreditor.gui.view.map;

import manfred.manfreditor.map.Map;
import org.springframework.stereotype.Component;

@Component
public class ViewCoordinateMapper {

    public MapViewCoordinate mapToBottomLeft(Map.TileCoordinate tileCoordinate) {
        Map.TileCoordinateWithInvertedY tileCoordinateWithInvertedY = tileCoordinate.invertY();

        return new MapViewCoordinate(
            tileCoordinateWithInvertedY.getX().times(TileViewSize.TILE_SIZE).value(),
            tileCoordinateWithInvertedY.getY().add(1).times(TileViewSize.TILE_SIZE).value() - 1
        );
    }
}
