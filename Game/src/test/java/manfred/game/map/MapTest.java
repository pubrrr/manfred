package manfred.game.map;

import helpers.TestMapFactory;
import manfred.data.infrastructure.map.MapPrototype;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MapTest {

    @Test
    public void oneXoneMap() {
        java.util.Map<MapPrototype.Coordinate, MapTile> mapTiles = new HashMap<>();
        mapTiles.put(new TestMapFactory.CoordinateDouble(0, 0), new Accessible());

        Map underTest = new Map(mapTiles);

        assertThat(underTest.sizeX(), is(1));
        assertThat(underTest.sizeY(), is(1));
    }

    @Test
    public void twoXthreeMap() {
        java.util.Map<MapPrototype.Coordinate, MapTile> mapTiles = new HashMap<>();
        mapTiles.put(new TestMapFactory.CoordinateDouble(0, 0), new Accessible());
        mapTiles.put(new TestMapFactory.CoordinateDouble(0, 1), new Accessible());
        mapTiles.put(new TestMapFactory.CoordinateDouble(0, 2), new Accessible());
        mapTiles.put(new TestMapFactory.CoordinateDouble(1, 0), new Accessible());
        mapTiles.put(new TestMapFactory.CoordinateDouble(1, 1), new Accessible());
        mapTiles.put(new TestMapFactory.CoordinateDouble(1, 2), new Accessible());

        Map underTest = new Map(mapTiles);

        assertThat(underTest.sizeX(), is(2));
        assertThat(underTest.sizeY(), is(3));
    }
}
