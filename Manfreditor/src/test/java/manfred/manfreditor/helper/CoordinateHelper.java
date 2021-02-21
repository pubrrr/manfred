package manfred.manfreditor.helper;

import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.shared.PositiveInt;

public class CoordinateHelper {

    private CoordinateHelper() {
    }

    public static MapPrototype.Coordinate coordinatePrototype(int x, int y) {
        return new CoordinateDouble(x, y);
    }

    public static class CoordinateDouble extends MapPrototype.Coordinate {
        public CoordinateDouble(int x, int y) {
            super(PositiveInt.of(x), PositiveInt.of(y));
        }
    }
}
