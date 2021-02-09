package manfred.data.infrastructure.map.validator;

import manfred.data.InvalidInputException;
import manfred.data.persistence.reader.UrlHelper;
import manfred.data.persistence.dto.RawMapDto;
import manfred.data.persistence.dto.TransporterDto;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.data.shared.PositiveInt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DoorsValidatorTest {

    private DoorsValidator underTest;
    private UrlHelper urlHelperMock;

    @BeforeEach
    void setUp() {
        urlHelperMock = mock(UrlHelper.class);
        underTest = new DoorsValidator(urlHelperMock);
    }

    @Test
    void noDoors() {
        RawMapDto input = getRawMapWithDoors();

        List<String> result = underTest.validate(input, nonAccessibleMap());

        assertThat(result, empty());
    }

    @Test
    void nonAccessibleMapIsValid() throws MalformedURLException, InvalidInputException {
        when(urlHelperMock.getResourceForMap(any())).thenReturn(Optional.of(new URL("http://some.url")));

        RawMapDto input = getRawMapWithDoors(new TransporterDto("target", PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0)));

        MapMatrix<TilePrototype> mapMatrixMock = nonAccessibleMap();
        List<String> result = underTest.validate(input, mapMatrixMock);

        assertThat(result, empty());
    }

    @Test
    void accessibleMapIsNotValid() throws MalformedURLException, InvalidInputException {
        when(urlHelperMock.getResourceForMap(any())).thenReturn(Optional.of(new URL("http://some.url")));

        RawMapDto input = getRawMapWithDoors(new TransporterDto("target", PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0)));

        MapMatrix<TilePrototype> mapMatrixMock = accessibleMap();
        List<String> result = underTest.validate(input, mapMatrixMock);

        assertThat(result, hasSize(1));
        assertThat(result, contains("Tile for door to target is accessible"));
    }

    @Test
    void accessibleAndAccessibleTile() throws MalformedURLException, InvalidInputException {
        when(urlHelperMock.getResourceForMap(any())).thenReturn(Optional.of(new URL("http://some.url")));

        RawMapDto input = getRawMapWithDoors(
            new TransporterDto("target1", PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0)),
            new TransporterDto("target2", PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(1))
        );

        MapMatrix<TilePrototype> mapMatrixMock = mock(MapMatrix.class);
        when(mapMatrixMock.get(eq(0), eq(0))).thenReturn(TilePrototype.accessible());
        when(mapMatrixMock.get(eq(0), eq(1))).thenReturn(TilePrototype.notAccessible());

        List<String> result = underTest.validate(input, mapMatrixMock);

        assertThat(result, hasSize(1));
        assertThat(result, contains("Tile for door to target1 is accessible"));
    }

    @Test
    void unknownResourceForTargetIsNotValid() throws InvalidInputException {
        when(urlHelperMock.getResourceForMap(any())).thenReturn(Optional.empty());

        RawMapDto input = getRawMapWithDoors(new TransporterDto("targetName", PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0)));

        List<String> result = underTest.validate(input, nonAccessibleMap());

        assertThat(result, hasSize(1));
        assertThat(result, contains("Resource for door target map targetName not found"));
    }

    @Test
    void unknownResourceAndNonAccessibleMap() throws InvalidInputException {
        when(urlHelperMock.getResourceForMap(any())).thenReturn(Optional.empty());

        RawMapDto input = getRawMapWithDoors(new TransporterDto("targetName", PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0)));

        List<String> result = underTest.validate(input, accessibleMap());

        assertThat(result, hasSize(2));
        assertThat(
            result,
            containsInAnyOrder("Tile for door to targetName is accessible", "Resource for door target map targetName not found")
        );
    }

    private RawMapDto getRawMapWithDoors(TransporterDto... doors) {
        return new RawMapDto("name", List.of(), List.of(), List.of(), Arrays.asList(doors), List.of());
    }

    private MapMatrix<TilePrototype> accessibleMap() {
        MapMatrix<TilePrototype> mapMatrixMock = mock(MapMatrix.class);
        when(mapMatrixMock.get(anyInt(), anyInt())).thenReturn(TilePrototype.accessible());
        return mapMatrixMock;
    }

    private MapMatrix<TilePrototype> nonAccessibleMap() {
        MapMatrix<TilePrototype> mapMatrixMock = mock(MapMatrix.class);
        when(mapMatrixMock.get(anyInt(), anyInt())).thenReturn(TilePrototype.notAccessible());
        return mapMatrixMock;
    }
}