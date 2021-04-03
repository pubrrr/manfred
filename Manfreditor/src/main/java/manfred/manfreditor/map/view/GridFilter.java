package manfred.manfreditor.map.view;

import java.util.Map;
import java.util.function.Predicate;

public class GridFilter {

    public static <S extends GridCoordinate, T> Predicate<Map.Entry<S, T>> tileWithSizeContains(int tilePixelSize, int clickedX, int clickedY) {
        return objectByGridCoordinate -> {
            GridCoordinate objectsGridCoordinate = objectByGridCoordinate.getKey();
            return objectsGridCoordinate.getX().value() >= clickedX / tilePixelSize
                && objectsGridCoordinate.getX().value() < clickedX / tilePixelSize + 1
                && objectsGridCoordinate.getY().value() >= clickedY / tilePixelSize
                && objectsGridCoordinate.getY().value() < clickedY / tilePixelSize + 1;
        };
    }
}
