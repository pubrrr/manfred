package manfred.manfreditor.gui.view;

import manfred.data.shared.PositiveInt;
import manfred.manfreditor.gui.view.map.MapViewCoordinate;
import manfred.manfreditor.gui.view.map.TileViewSize;
import manfred.manfreditor.gui.view.map.ViewCoordinateMapper;
import manfred.manfreditor.map.Map.TileCoordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ViewCoordinateMapperTest {

    private ViewCoordinateMapper underTest;

    @BeforeEach
    void setUp() {
        underTest = new ViewCoordinateMapper();
    }

    @Test
    void givenSize1_topLeftInput() {
        MapViewCoordinate result = underTest.mapToBottomLeft(mockCoordinate(0, 0), PositiveInt.ofNonZero(1));

        assertThat(result, equalTo(new MapViewCoordinate(0, TileViewSize.TILE_SIZE - 1)));
    }

    @Test
    void givenSize1_inputSomehwereMoreRight() {
        MapViewCoordinate result = underTest.mapToBottomLeft(mockCoordinate(2, 0), PositiveInt.ofNonZero(1));

        assertThat(result, equalTo(new MapViewCoordinate(2 * TileViewSize.TILE_SIZE, TileViewSize.TILE_SIZE - 1)));
    }

    @Test
    void givenSize3_inputTopRight() {
        MapViewCoordinate result = underTest.mapToBottomLeft(mockCoordinate(2, 2), PositiveInt.ofNonZero(3));

        assertThat(result, equalTo(new MapViewCoordinate(2 * TileViewSize.TILE_SIZE, TileViewSize.TILE_SIZE - 1)));
    }

    @Test
    void givenSize3_inputBottomRight() {
        MapViewCoordinate result = underTest.mapToBottomLeft(mockCoordinate(2, 0), PositiveInt.ofNonZero(3));

        assertThat(result, equalTo(new MapViewCoordinate(2 * TileViewSize.TILE_SIZE, 3 * TileViewSize.TILE_SIZE - 1)));
    }

    private TileCoordinate mockCoordinate(int x, int y) {
        TileCoordinate coordinateMock = mock(TileCoordinate.class);
        when(coordinateMock.getX()).thenReturn(PositiveInt.of(x));
        when(coordinateMock.getY()).thenReturn(PositiveInt.of(y));
        return coordinateMock;
    }
}