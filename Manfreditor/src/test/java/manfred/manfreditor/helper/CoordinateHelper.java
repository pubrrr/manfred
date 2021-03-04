package manfred.manfreditor.helper;

import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.data.shared.PositiveInt;
import manfred.manfreditor.map.Map;

import java.util.ArrayList;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CoordinateHelper {

    private CoordinateHelper() {
    }

    public static MapPrototype mockMapPrototype(java.util.Map<MapPrototype.Coordinate, TilePrototype> tiles) {
        MapPrototype mock = mock(MapPrototype.class);
        when(mock.getCoordinateSet()).thenReturn(new ArrayList<>(tiles.keySet()));
        tiles.forEach((coordinate, tilePrototype) -> when(mock.getFromMap(eq(coordinate))).thenReturn(tilePrototype));
        return mock;
    }

    public static MapPrototype.Coordinate coordinatePrototype(int x, int y) {
        return new CoordinateDouble(x, y);
    }

    public static Map.TileCoordinate tileCoordinate(int x, int y) {
        return new TileCoordinateDouble(x, y, new Map("test", new HashMap<>()));
    }

    public static Map.TileCoordinate tileCoordinate(int x, int y, int mapSizeY) {
        Map mapMock = mock(Map.class);
        when(mapMock.getSizeY()).thenReturn(PositiveInt.of(mapSizeY));
        return new TileCoordinateDouble(x, y, mapMock);
    }

    public static class CoordinateDouble extends MapPrototype.Coordinate {
        public CoordinateDouble(int x, int y) {
            super(PositiveInt.of(x), PositiveInt.of(y));
        }
    }

    public static class TileCoordinateDouble extends Map.TileCoordinate {
        protected TileCoordinateDouble(int x, int y, Map map) {
            map.super(PositiveInt.of(x), PositiveInt.of(y));
        }
    }
}
