package manfred.manfreditor.gui.view.mapobject;

import manfred.data.shared.PositiveInt;

import java.util.ArrayList;
import java.util.List;

public class ObjectsViewCoordinateFactory {

    private final int numberOfColumns;

    public ObjectsViewCoordinateFactory(PositiveInt.Strict numberOfColumns) {
        this.numberOfColumns = numberOfColumns.value();
    }

    public List<ObjectsGridCoordinate> getCoordinates(PositiveInt numberOfObjects) {
        List<ObjectsGridCoordinate> coordinates = new ArrayList<>(numberOfObjects.value());

        for (int i = 0; i < numberOfObjects.value(); i++) {
            coordinates.add(new ObjectsGridCoordinate(PositiveInt.of(i % numberOfColumns), PositiveInt.of(i / numberOfColumns)));
        }
        return coordinates;
    }
}
