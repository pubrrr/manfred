package manfred.manfreditor.gui.view.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static manfred.manfreditor.helper.CoordinateHelper.tileCoordinate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ViewCoordinateMapperTest {

    private ViewCoordinateMapper underTest;

    @BeforeEach
    void setUp() {
        underTest = new ViewCoordinateMapper();
    }

    @Test
    void givenSize1_topLeftInput() {
        MapViewCoordinate result = underTest.mapToBottomLeft(tileCoordinate(0, 0, 1));

        assertThat(result, equalTo(new MapViewCoordinate(0, TileViewSize.TILE_SIZE - 1)));
    }

    @Test
    void givenSize1_inputSomehwereMoreRight() {
        MapViewCoordinate result = underTest.mapToBottomLeft(tileCoordinate(2, 0, 1));

        assertThat(result, equalTo(new MapViewCoordinate(2 * TileViewSize.TILE_SIZE, TileViewSize.TILE_SIZE - 1)));
    }

    @Test
    void givenSize3_inputTopRight() {
        MapViewCoordinate result = underTest.mapToBottomLeft(tileCoordinate(2, 2, 3));

        assertThat(result, equalTo(new MapViewCoordinate(2 * TileViewSize.TILE_SIZE, TileViewSize.TILE_SIZE - 1)));
    }

    @Test
    void givenSize3_inputBottomRight() {
        MapViewCoordinate result = underTest.mapToBottomLeft(tileCoordinate(2, 0, 3));

        assertThat(result, equalTo(new MapViewCoordinate(2 * TileViewSize.TILE_SIZE, 3 * TileViewSize.TILE_SIZE - 1)));
    }
}