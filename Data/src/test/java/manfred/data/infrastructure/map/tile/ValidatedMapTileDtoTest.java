package manfred.data.infrastructure.map.tile;

import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.shared.PositiveInt;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ValidatedMapTileDtoTest {

    @Test
    void getOriginCoordinate_everyTileIsAccessible_thenBottomLeftIsReturned() {
        var structureMock = mock(MapPrototype.class);
        List<MapPrototype.Coordinate> coordinates = List.of(mockCoordinate(0, 0));
        when(structureMock.getCoordinateSet()).thenReturn(coordinates);
        when(structureMock.getFromMap(any())).thenReturn(TilePrototype.accessible());
        MapPrototype.Coordinate bottomLeftCoordinate = mockCoordinate(99, 99);
        when(structureMock.getBottomLeftCoordinate()).thenReturn(bottomLeftCoordinate);

        var underTest = new ValidatedMapTileDto(null, structureMock, null, null);
        MapPrototype.Coordinate result = underTest.getOriginCoordinate();

        assertThat(result, is(bottomLeftCoordinate));
    }

    @Test
    void getOriginCoordinate_onlyCoordinatesWithXEqual0AreAllowed() {
        var structureMock = mock(MapPrototype.class);
        List<MapPrototype.Coordinate> coordinates = List.of(mockCoordinate(1, 0));
        when(structureMock.getCoordinateSet()).thenReturn(coordinates);
        when(structureMock.getFromMap(any())).thenReturn(TilePrototype.notAccessible());
        MapPrototype.Coordinate bottomLeftCoordinate = mockCoordinate(99, 99);
        when(structureMock.getBottomLeftCoordinate()).thenReturn(bottomLeftCoordinate);

        var underTest = new ValidatedMapTileDto(null, structureMock, null, null);
        MapPrototype.Coordinate result = underTest.getOriginCoordinate();

        assertThat(result, is(bottomLeftCoordinate));
    }

    @Test
    void getOriginCoordinate_oneCoordinateWhosTileIsNotAccessible() {
        var structureMock = mock(MapPrototype.class);
        MapPrototype.Coordinate coordinate = mockCoordinate(0, 0);
        when(structureMock.getCoordinateSet()).thenReturn(List.of(coordinate));
        when(structureMock.getFromMap(any())).thenReturn(TilePrototype.notAccessible());

        var underTest = new ValidatedMapTileDto(null, structureMock, null, null);
        MapPrototype.Coordinate result = underTest.getOriginCoordinate();

        assertThat(result, is(coordinate));
    }

    @Test
    void getOriginCoordinate_bottomLeftAccessible_thenCoordinateTopOfItIsReturned() {
        var structureMock = mock(MapPrototype.class);
        MapPrototype.Coordinate coordinate_0_0 = mockCoordinate(0, 0);
        MapPrototype.Coordinate coordinate_0_1 = mockCoordinate(0, 1);
        when(structureMock.getCoordinateSet()).thenReturn(List.of(coordinate_0_0, coordinate_0_1));
        when(structureMock.getFromMap(eq(coordinate_0_0))).thenReturn(TilePrototype.accessible());
        when(structureMock.getFromMap(eq(coordinate_0_1))).thenReturn(TilePrototype.notAccessible());

        var underTest = new ValidatedMapTileDto(null, structureMock, null, null);
        MapPrototype.Coordinate result = underTest.getOriginCoordinate();

        assertThat(result, is(coordinate_0_1));
    }

    @Test
    void getOriginCoordinate_twoCoordinatesAreNotAccessbile_thenBottommostIsReturend() {
        var structureMock = mock(MapPrototype.class);
        MapPrototype.Coordinate coordinate_0_0 = mockCoordinate(0, 0);
        MapPrototype.Coordinate coordinate_0_1 = mockCoordinate(0, 1);
        when(structureMock.getCoordinateSet()).thenReturn(List.of(coordinate_0_0, coordinate_0_1));
        when(structureMock.getFromMap(eq(coordinate_0_0))).thenReturn(TilePrototype.notAccessible());
        when(structureMock.getFromMap(eq(coordinate_0_1))).thenReturn(TilePrototype.notAccessible());

        var underTest = new ValidatedMapTileDto(null, structureMock, null, null);
        MapPrototype.Coordinate result = underTest.getOriginCoordinate();

        assertThat(result, is(coordinate_0_0));
    }

    private MapPrototype.Coordinate mockCoordinate(int x, int y) {
        MapPrototype.Coordinate coordinateMock = mock(MapPrototype.Coordinate.class);
        when(coordinateMock.getX()).thenReturn(PositiveInt.of(x));
        when(coordinateMock.getY()).thenReturn(PositiveInt.of(y));
        return coordinateMock;
    }
}