package manfred.data.infrastructure.map;

import manfred.data.InvalidInputException;
import manfred.data.infrastructure.enemy.EnemiesLoader;
import manfred.data.infrastructure.enemy.EnemyPrototype;
import manfred.data.infrastructure.map.tile.MapTileReader;
import manfred.data.infrastructure.map.tile.ObjectTileConverter;
import manfred.data.infrastructure.map.validator.Validator;
import manfred.data.infrastructure.person.PersonPrototype;
import manfred.data.infrastructure.person.PersonsLoader;
import manfred.data.infrastructure.person.gelaber.GelaberPrototype;
import manfred.data.persistence.dto.MapEnemyDto;
import manfred.data.persistence.dto.MapPersonDto;
import manfred.data.persistence.dto.RawMapDto;
import manfred.data.persistence.dto.TransporterDto;
import manfred.data.shared.PositiveInt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MapValidatorTest {

    private MapValidator underTest;

    private Validator validatorMock1;
    private Validator validatorMock2;
    private EnemiesLoader enemiesLoaderMock;
    private PersonsLoader personsLoaderMock;

    @BeforeEach
    void setUp() {
        validatorMock1 = mock(Validator.class);
        validatorMock2 = mock(Validator.class);
        enemiesLoaderMock = mock(EnemiesLoader.class);
        personsLoaderMock = mock(PersonsLoader.class);

        underTest = new MapValidator(List.of(validatorMock1, validatorMock2), new ObjectTileConverter(mock(MapTileReader.class)), enemiesLoaderMock, personsLoaderMock);
    }

    @Test
    void emptyOtherStructs() throws InvalidInputException {
        RawMapDto input = new RawMapDto("test", List.of("1"), List.of(), List.of(), List.of(), List.of(), null);

        MapPrototype result = underTest.validate(input);

        MapPrototype.Coordinate coordinate = result.getCoordinateSet().get(0);

        assertTrue(result.getPerson(coordinate).isEmpty());
        assertTrue(result.getPortal(coordinate).isEmpty());
        assertTrue(result.getDoor(coordinate).isEmpty());
        assertThat(result.getEnemies(), empty());
    }

    @Test
    void nonEmptyOtherStructs() throws InvalidInputException {
        when(enemiesLoaderMock.load(any())).thenReturn(List.of(new EnemyPrototype("name", PositiveInt.of(0), PositiveInt.of(0), null, PositiveInt.of(0), PositiveInt.of(0))));
        when(personsLoaderMock.load(any())).thenReturn(List.of(new PersonPrototype("name", mock(GelaberPrototype.class), null, PositiveInt.of(0), PositiveInt.of(0))));

        RawMapDto input = new RawMapDto(
            "test",
            List.of("1,1"),
            List.of(new MapPersonDto()),
            List.of(new TransporterDto("", PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0))),
            List.of(
                new TransporterDto("", PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0)),
                new TransporterDto("", PositiveInt.of(1), PositiveInt.of(0), PositiveInt.of(1), PositiveInt.of(0))
            ),
            List.of(new MapEnemyDto()),
            null
        );

        MapPrototype result = underTest.validate(input);

        List<MapPrototype.Coordinate> coordinates = result.getCoordinateSet();

        List<PersonPrototype> persons = coordinates.stream().map(result::getPerson).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
        List<TransporterDto> portal = coordinates.stream().map(result::getPortal).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
        List<TransporterDto> doors = coordinates.stream().map(result::getDoor).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());

        assertThat(persons, hasSize(1));
        assertThat(portal, hasSize(1));
        assertThat(doors, hasSize(2));
        assertThat(result.getEnemies(), hasSize(1));
    }

    @Test
    void unknownEnemy() throws InvalidInputException {
        when(personsLoaderMock.load(any())).thenReturn(List.of());
        when(enemiesLoaderMock.load(any())).thenThrow(new InvalidInputException("errorMessage"));

        RawMapDto input = new RawMapDto(
            "test",
            List.of("1"),
            List.of(),
            List.of(),
            List.of(),
            List.of(new MapEnemyDto()),
            null
        );

        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> underTest.validate(input));
        assertThat(exception.getMessage(), containsString("Error when creating objects for map test"));
        assertThat(exception.getCause().getMessage(), containsString("errorMessage"));
    }

    @Test
    void unknownPerson() throws InvalidInputException {
        when(personsLoaderMock.load(any())).thenThrow(new InvalidInputException("personErrorMessage"));
        when(enemiesLoaderMock.load(any())).thenReturn(List.of());

        RawMapDto input = new RawMapDto(
            "test",
            List.of("1"),
            List.of(),
            List.of(),
            List.of(),
            List.of(new MapEnemyDto()),
            null
        );

        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> underTest.validate(input));
        assertThat(exception.getMessage(), containsString("Error when creating objects for map test"));
        assertThat(exception.getCause().getMessage(), containsString("personErrorMessage"));
    }

    @Test
    void oneColumnMap() throws InvalidInputException {
        RawMapDto input = new RawMapDto(
            "test",
            List.of(
                "0",
                "1"
            ),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            null
        );

        MapPrototype result = underTest.validate(input);

        List<MapPrototype.Coordinate> coordinateSet = result.getCoordinateSet();
        assertThat(coordinateSet, hasSize(2));
        assertThat(coordinateSet.get(1).getX().value(), is(0));
        assertThat(coordinateSet.get(1).getY().value(), is(1));
    }

    @Test
    void oneRowMap() throws InvalidInputException {
        RawMapDto input = new RawMapDto(
            "test",
            List.of(
                "0,1"
            ),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            null
        );

        MapPrototype result = underTest.validate(input);

        List<MapPrototype.Coordinate> coordinateSet = result.getCoordinateSet();
        assertThat(coordinateSet, hasSize(2));
        assertThat(coordinateSet.get(1).getX().value(), is(1));
        assertThat(coordinateSet.get(1).getY().value(), is(0));
    }

    @Test
    void twoXTwoMap() throws InvalidInputException {
        RawMapDto input = new RawMapDto(
            "test",
            List.of(
                "0,1",
                "0,1"
            ),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            null
        );

        MapPrototype result = underTest.validate(input);

        List<MapPrototype.Coordinate> coordinateSet = result.getCoordinateSet();
        assertThat(coordinateSet, hasSize(4));
        assertThat(coordinateSet.get(3).getX().value(), is(1));
        assertThat(coordinateSet.get(3).getY().value(), is(1));
        assertThat(result.getFromMap(getCoordinate(coordinateSet, 0, 1)).isAccessible(), is(false));
        assertThat(result.getFromMap(getCoordinate(coordinateSet, 1, 0)).isAccessible(), is(true));
    }

    @Test
    void twoXTwoMap_withStringToTrim() throws InvalidInputException {
        RawMapDto input = new RawMapDto(
            "test",
            List.of(
                "0 ,1",
                "0, 1 "
            ),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            null
        );

        MapPrototype result = underTest.validate(input);

        List<MapPrototype.Coordinate> coordinateSet = result.getCoordinateSet();
        assertThat(coordinateSet, hasSize(4));
        assertThat(coordinateSet.get(3).getX().value(), is(1));
        assertThat(coordinateSet.get(3).getY().value(), is(1));
        assertThat(result.getFromMap(getCoordinate(coordinateSet, 0, 1)).isAccessible(), is(false));
        assertThat(result.getFromMap(getCoordinate(coordinateSet, 1, 0)).isAccessible(), is(true));
    }

    @Test
    void twoRowsThreeColumnsMap() throws InvalidInputException {
        RawMapDto input = new RawMapDto(
            "test",
            List.of(
                "0,1,0",
                "1,0,1"
            ),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            null
        );

        MapPrototype result = underTest.validate(input);

        List<MapPrototype.Coordinate> coordinateSet = result.getCoordinateSet();
        assertThat(coordinateSet, hasSize(6));
        assertThat(result.getFromMap(getCoordinate(coordinateSet, 0, 1)).isAccessible(), is(false));
        assertThat(result.getFromMap(getCoordinate(coordinateSet, 1, 1)).isAccessible(), is(true));
        assertThat(result.getFromMap(getCoordinate(coordinateSet, 2, 1)).isAccessible(), is(false));
    }

    @Test
    void rowTooLong() {
        RawMapDto input = new RawMapDto(
            "test",
            List.of(
                "0, 1",
                "0, 1",
                "0, 1,1",
                "0, 1"
            ),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            null
        );

        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> underTest.validate(input));
        assertThat(exception.getMessage(), containsString("Could not create map test"));
        assertThat(exception.getCause().getMessage(), containsString("Map row 2 is too long"));
        assertThat(exception.getCause().getMessage(), containsString("must not be longer than 2"));
    }

    @Test
    void twoRowsTooLong() {
        RawMapDto input = new RawMapDto(
            "test",
            List.of(
                "0,2",
                "0",
                "0,1",
                "0"
            ),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            null
        );

        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> underTest.validate(input));
        assertThat(exception.getMessage(), containsString("Could not create map test"));
        assertThat(exception.getCause().getMessage(), containsString("Map row 0 is too long"));
        assertThat(exception.getCause().getMessage(), containsString("Map row 2 is too long"));
        assertThat(exception.getCause().getMessage(), containsString("must not be longer than 1"));
    }

    @Test
    void oneFailingValidator() {
        RawMapDto input = new RawMapDto("test", List.of("1"), List.of(), List.of(), List.of(), List.of(), null);

        when(validatorMock1.validate(any(), any())).thenReturn(List.of("message1", "message2"));

        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> underTest.validate(input));
        assertThat(exception.getMessage(), containsString("Validation of map test failed:\nmessage1,\nmessage2"));
    }

    @Test
    void twoFailingValidators() {
        RawMapDto input = new RawMapDto("test", List.of("1"), List.of(), List.of(), List.of(), List.of(), null);

        when(validatorMock1.validate(any(), any())).thenReturn(List.of("validator1_message1", "validator1_message2"));
        when(validatorMock2.validate(any(), any())).thenReturn(List.of("validator2_message1", "validator2_message2"));

        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> underTest.validate(input));
        assertThat(exception.getMessage(), containsString("validator1_message1,\nvalidator1_message2"));
        assertThat(exception.getMessage(), containsString("validator2_message1,\nvalidator2_message2"));
    }

    private MapPrototype.Coordinate getCoordinate(List<MapPrototype.Coordinate> coordinateSet, int x, int y) {
        return coordinateSet.stream()
            .filter(coordinate -> coordinate.getX().value() == x && coordinate.getY().value() == y)
            .findAny()
            .orElseThrow(() -> new AssertionFailedError("Coordinate (" + x + "," + y + ") not found"));
    }
}