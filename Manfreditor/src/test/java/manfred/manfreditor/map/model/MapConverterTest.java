package manfred.manfreditor.map.model;

import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.TileConversionRule;
import manfred.data.shared.PositiveInt;
import manfred.manfreditor.map.model.mapobject.MapObject;
import manfred.manfreditor.map.model.mapobject.None;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static manfred.manfreditor.helper.CoordinateHelper.coordinatePrototype;
import static manfred.manfreditor.helper.CoordinateHelper.tileCoordinate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MapConverterTest {

    private MapConverter underTest;
    private TileConversionRule<MapObject> tileConversionRuleMock;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        tileConversionRuleMock = mock(TileConversionRule.class);
        underTest = new MapConverter(tileConversionRuleMock);
    }

    @Test
    void convertWhenNoRuleApplies() {
        MapPrototype input = mock(MapPrototype.class);
        when(input.getCoordinateSet()).thenReturn(List.of(coordinate(0, 0)));
        when(input.getName()).thenReturn("name");

        when(tileConversionRuleMock.applicableTo(any(), any())).thenReturn(Optional.empty());

        Map result = underTest.convert(input);

        assertThat(result.getName(), is("name"));
        assertThat(result.getSizeX(), is(PositiveInt.of(1)));
        assertThat(result.getSizeY(), is(PositiveInt.of(1)));
        assertThat(result.getObjectAt(tileCoordinate(0, 0)), instanceOf(None.class));
    }

    @Test
    void convertEmptyOneTileMap() {
        MapPrototype input = mock(MapPrototype.class);
        when(input.getCoordinateSet()).thenReturn(List.of(coordinate(0, 0)));
        when(input.getName()).thenReturn("name");

        MapObject mapObjectMock = mock(MapObject.class);
        when(tileConversionRuleMock.applicableTo(any(), any())).thenReturn(Optional.of(() -> mapObjectMock));

        Map result = underTest.convert(input);

        assertThat(result.getName(), is("name"));
        assertThat(result.getSizeX(), is(PositiveInt.of(1)));
        assertThat(result.getSizeY(), is(PositiveInt.of(1)));
        assertThat(result.getObjectAt(tileCoordinate(0, 0)), is(mapObjectMock));
    }

    @Test
    void convertEmptyMultipleTilesMap() {
        MapPrototype input = mock(MapPrototype.class);
        when(input.getCoordinateSet()).thenReturn(List.of(
            coordinate(0, 0),
            coordinate(1, 0),
            coordinate(2, 0),
            coordinate(0, 1),
            coordinate(1, 1),
            coordinate(2, 1)
        ));
        when(input.getName()).thenReturn("name");

        when(tileConversionRuleMock.applicableTo(any(), any())).thenReturn(Optional.of(() -> mock(MapObject.class)));

        Map result = underTest.convert(input);

        assertThat(result.getName(), is("name"));
        assertThat(result.getSizeX(), is(PositiveInt.of(3)));
        assertThat(result.getSizeY(), is(PositiveInt.of(2)));
    }

    private MapPrototype.Coordinate coordinate(int x, int y) {
        return coordinatePrototype(x, y);
    }

}