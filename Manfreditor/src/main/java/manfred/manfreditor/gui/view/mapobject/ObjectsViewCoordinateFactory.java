package manfred.manfreditor.gui.view.mapobject;

import manfred.data.shared.PositiveInt;

import java.util.ArrayList;
import java.util.List;

public class ObjectsViewCoordinateFactory {

    private final int numberOfColumns;

    public ObjectsViewCoordinateFactory(PositiveInt.Strict numberOfColumns) {
        this.numberOfColumns = numberOfColumns.value();
    }

    public List<ObjectsViewCoordinate> getCoordinates(PositiveInt numberOfObjects) {
        List<ObjectsViewCoordinate> coordinates = new ArrayList<>(numberOfObjects.value());

        for (int i = 0; i < numberOfObjects.value(); i++) {
            coordinates.add(new ObjectsViewCoordinate(i % numberOfColumns, i / numberOfColumns));
        }
        return coordinates;
    }
}
