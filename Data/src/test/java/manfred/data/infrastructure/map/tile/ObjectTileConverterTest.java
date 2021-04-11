package manfred.data.infrastructure.map.tile;

import manfred.data.InvalidInputException;
import manfred.data.infrastructure.map.MapPrototype;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ObjectTileConverterTest {

    private ObjectTileConverter underTest;

    private MapTileReader mapTileReaderMock;

    @BeforeEach
    void setUp() {
        mapTileReaderMock = mock(MapTileReader.class);
        underTest = new ObjectTileConverter(mapTileReaderMock);
    }

    @Test
    void accessible() {
        TilePrototype tilePrototype = underTest.stringToObject("1");

        assertTrue(tilePrototype.isAccessible());
        assertFalse(tilePrototype.getTileObject().isPresent());
    }

    @Test
    void notAccessible_0() {
        TilePrototype tilePrototype = underTest.stringToObject("0");

        assertFalse(tilePrototype.isAccessible());
        assertFalse(tilePrototype.getTileObject().isPresent());
    }

    @Test
    void notAccessible_underscore() {
        TilePrototype tilePrototype = underTest.stringToObject("_startingWithUnderscore");

        assertFalse(tilePrototype.isAccessible());
        assertFalse(tilePrototype.getTileObject().isPresent());
    }

    @Test
    void accessibleObject() throws InvalidInputException {
        MapTileStructurePrototype matrixMock = mockMapMatrix();
        MapPrototype.Coordinate originCoordinate = mock(MapPrototype.Coordinate.class);
        when(matrixMock.getFromMap(eq(originCoordinate))).thenReturn(TilePrototype.accessible());
        ValidatedMapTileDto mapTileDto = mock(ValidatedMapTileDto.class);
        when(mapTileDto.getOriginCoordinate()).thenReturn(originCoordinate);
        when(mapTileDto.getStructure()).thenReturn(matrixMock);

        when(mapTileReaderMock.load("tileValue")).thenReturn(mapTileDto);

        TilePrototype tilePrototype = underTest.stringToObject("tileValue");

        assertTrue(tilePrototype.isAccessible());
        assertTrue(tilePrototype.getTileObject().isPresent());
        assertThat(tilePrototype.getTileObject().get(), is(mapTileDto));
    }

    @Test
    void notAccessibleObject() throws InvalidInputException {
        MapTileStructurePrototype matrixMock = mockMapMatrix();
        MapPrototype.Coordinate originCoordinate = mock(MapPrototype.Coordinate.class);
        when(matrixMock.getFromMap(eq(originCoordinate))).thenReturn(TilePrototype.notAccessible());
        ValidatedMapTileDto mapTileDto = mock(ValidatedMapTileDto.class);
        when(mapTileDto.getOriginCoordinate()).thenReturn(originCoordinate);
        when(mapTileDto.getStructure()).thenReturn(matrixMock);

        when(mapTileReaderMock.load("tileValue")).thenReturn(mapTileDto);

        TilePrototype tilePrototype = underTest.stringToObject("tileValue");

        assertFalse(tilePrototype.isAccessible());
        assertTrue(tilePrototype.getTileObject().isPresent());
        assertThat(tilePrototype.getTileObject().get(), is(mapTileDto));
    }

    private MapTileStructurePrototype mockMapMatrix() {
        return mock(MapTileStructurePrototype.class);
    }
}