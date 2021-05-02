package manfred.manfreditor.map.model.export;

import io.vavr.collection.HashMap;
import manfred.data.persistence.dto.RawMapDto;
import manfred.data.persistence.reader.MapSource;
import manfred.manfreditor.map.model.accessibility.ColoredAccessibilityIndicator;
import manfred.manfreditor.map.model.accessibility.EmptyAccessibilityIndicator;
import manfred.manfreditor.map.model.accessibility.Source;
import manfred.manfreditor.map.model.flattened.FlattenedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static manfred.manfreditor.helper.CoordinateHelper.tileCoordinate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

class MapToDtoMapperTest {

    private MapToDtoMapper underTest;

    @BeforeEach
    void setUp() {
        underTest = new MapToDtoMapper();
    }

    @Test
    void emptyMap() {
        var input = new FlattenedMap("name", HashMap.empty(), someMapSource());

        RawMapDto result = underTest.map(input);

        assertThat(result.getName(), is("name"));
        assertThat(result.getMap(), empty());
        assertThat(result.getPersons(), empty());
        assertThat(result.getDoors(), empty());
        assertThat(result.getPortals(), empty());
        assertThat(result.getEnemies(), empty());
    }

    @Test
    void mapWithOneAccessibleTile() {
        var input = new FlattenedMap("name", HashMap.of(tileCoordinate(0, 0), new EmptyAccessibilityIndicator()), someMapSource());

        RawMapDto result = underTest.map(input);

        assertThat(result.getMap(), hasSize(1));
        assertThat(result.getMap().get(0), is("1"));
    }

    @Test
    void mapWithOneNonAccessibleTileWithSourceAtSameTile() {
        var input = new FlattenedMap("name", HashMap.of(
            tileCoordinate(0, 0),
            new ColoredAccessibilityIndicator(null, new Source("sourceName", tileCoordinate(0, 0)))
        ), someMapSource());

        RawMapDto result = underTest.map(input);

        assertThat(result.getMap(), hasSize(1));
        assertThat(result.getMap().get(0), is("sourceName"));
    }

    @Test
    void mapWithOneNonAccessibleTileWithSourceAtAnotherTile() {
        var input = new FlattenedMap("name", HashMap.of(
            tileCoordinate(0, 0),
            new ColoredAccessibilityIndicator(null, new Source("sourceName", tileCoordinate(77, 99)))
        ), someMapSource());

        RawMapDto result = underTest.map(input);

        assertThat(result.getMap(), hasSize(1));
        assertThat(result.getMap().get(0), is("_sourceName"));
    }

    @Test
    void mapTwoCoordinatesInSameRow() {
        var input = new FlattenedMap("name", HashMap.of(
            tileCoordinate(0, 0), new EmptyAccessibilityIndicator(),
            tileCoordinate(1, 0), new EmptyAccessibilityIndicator()
        ), someMapSource());

        RawMapDto result = underTest.map(input);

        assertThat(result.getMap(), hasSize(1));
        assertThat(result.getMap().get(0), is("1,1"));
    }

    @Test
    void mapTwoCoordinatesInSameRow_appearInCorrectOrder() {
        var input = new FlattenedMap("name", HashMap.of(
            tileCoordinate(1, 0), new ColoredAccessibilityIndicator(null, new Source("tile", tileCoordinate(0, 0))),
            tileCoordinate(0, 0), new EmptyAccessibilityIndicator()
        ), someMapSource());

        RawMapDto result = underTest.map(input);

        assertThat(result.getMap(), hasSize(1));
        assertThat(result.getMap().get(0), is("1,_tile"));
    }

    @Test
    void mapTwoCoordinatesInSameColumn_appearInCorrectOrder() {
        var input = new FlattenedMap("name", HashMap.of(
            tileCoordinate(0, 1), new ColoredAccessibilityIndicator(null, new Source("tile", tileCoordinate(0, 0))),
            tileCoordinate(0, 0), new EmptyAccessibilityIndicator()
        ), someMapSource());

        RawMapDto result = underTest.map(input);

        assertThat(result.getMap(), hasSize(2));
        assertThat(result.getMap(), contains("_tile", "1"));
    }

    @Test
    void map4x4Map() {
        var input = new FlattenedMap("name", HashMap.of(
            tileCoordinate(0, 0), new EmptyAccessibilityIndicator(),
            tileCoordinate(1, 0), new ColoredAccessibilityIndicator(null, new Source("tile", tileCoordinate(1, 0))),
            tileCoordinate(0, 1), new ColoredAccessibilityIndicator(null, new Source("tile", tileCoordinate(1, 0))),
            tileCoordinate(1, 1), new ColoredAccessibilityIndicator(null, new Source("tile", tileCoordinate(1, 0)))
        ), someMapSource());

        RawMapDto result = underTest.map(input);

        assertThat(result.getName(), is("name"));
        assertThat(result.getMap(), hasSize(2));
        assertThat(result.getMap(), contains("_tile,_tile", "1,tile"));
    }

    private MapSource someMapSource() {
        return new MapSource(new File(""));
    }
}