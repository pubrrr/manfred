package manfred.data.infrastructure.map;

import manfred.data.InvalidInputException;
import manfred.data.infrastructure.enemy.EnemiesLoader;
import manfred.data.infrastructure.enemy.LocatedEnemyDto;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.MapTileReader;
import manfred.data.infrastructure.map.tile.TileConverter;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.data.infrastructure.map.validator.Validator;
import manfred.data.infrastructure.person.PersonPrototype;
import manfred.data.infrastructure.person.PersonsLoader;
import manfred.data.infrastructure.person.gelaber.GelaberPrototype;
import manfred.data.persistence.dto.MapEnemyDto;
import manfred.data.persistence.dto.MapPersonDto;
import manfred.data.persistence.dto.RawMapDto;
import manfred.data.persistence.dto.TransporterDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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

        underTest = new MapValidator(List.of(validatorMock1, validatorMock2), new TileConverter(mock(MapTileReader.class)), enemiesLoaderMock, personsLoaderMock);
    }

    @Test
    void emptyOtherStructs() throws InvalidInputException {
        RawMapDto input = new RawMapDto("test", List.of("1"), List.of(), List.of(), List.of(), List.of());

        MapPrototype result = underTest.validate(input);

        assertThat(result.getPersons(), empty());
        assertThat(result.getPortals(), empty());
        assertThat(result.getDoors(), empty());
        assertThat(result.getEnemies(), empty());
    }

    @Test
    void nonEmptyOtherStructs() throws InvalidInputException {
        when(enemiesLoaderMock.load(any())).thenReturn(List.of(new LocatedEnemyDto("name", 0, 0, null, 0, 0)));
        when(personsLoaderMock.load(any())).thenReturn(List.of(new PersonPrototype("name", mock(GelaberPrototype.class), null, 0, 0)));

        RawMapDto input = new RawMapDto(
            "test",
            List.of("1"),
            List.of(new MapPersonDto()),
            List.of(new TransporterDto()),
            List.of(new TransporterDto(), new TransporterDto()),
            List.of(new MapEnemyDto())
        );

        MapPrototype result = underTest.validate(input);

        assertThat(result.getPersons(), hasSize(1));
        assertThat(result.getPortals(), hasSize(1));
        assertThat(result.getDoors(), hasSize(2));
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
            List.of(new MapEnemyDto())
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
            List.of(new MapEnemyDto())
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
            List.of()
        );

        MapPrototype result = underTest.validate(input);

        MapMatrix<TilePrototype> matrix = result.getMap();
        assertThat(matrix.sizeX(), is(1));
        assertThat(matrix.sizeY(), is(2));
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
            List.of()
        );

        MapPrototype result = underTest.validate(input);

        MapMatrix<TilePrototype> matrix = result.getMap();
        assertThat(matrix.sizeX(), is(2));
        assertThat(matrix.sizeY(), is(1));
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
            List.of()
        );

        MapPrototype result = underTest.validate(input);

        MapMatrix<TilePrototype> matrix = result.getMap();
        assertThat(matrix.sizeX(), is(2));
        assertThat(matrix.sizeY(), is(2));
        assertThat(matrix.get(0, 1).isAccessible(), is(false));
        assertThat(matrix.get(1, 0).isAccessible(), is(true));
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
            List.of()
        );

        MapPrototype result = underTest.validate(input);

        MapMatrix<TilePrototype> matrix = result.getMap();
        assertThat(matrix.sizeX(), is(2));
        assertThat(matrix.sizeY(), is(2));
        assertThat(matrix.get(0, 1).isAccessible(), is(false));
        assertThat(matrix.get(1, 0).isAccessible(), is(true));
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
            List.of()
        );

        MapPrototype result = underTest.validate(input);

        MapMatrix<TilePrototype> matrix = result.getMap();
        assertThat(matrix.sizeX(), is(3));
        assertThat(matrix.sizeY(), is(2));
        assertThat(matrix.get(0, 1).isAccessible(), is(true));
        assertThat(matrix.get(1, 1).isAccessible(), is(false));
        assertThat(matrix.get(2, 1).isAccessible(), is(true));
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
            List.of()
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
            List.of()
        );

        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> underTest.validate(input));
        assertThat(exception.getMessage(), containsString("Could not create map test"));
        assertThat(exception.getCause().getMessage(), containsString("Map row 0 is too long"));
        assertThat(exception.getCause().getMessage(), containsString("Map row 2 is too long"));
        assertThat(exception.getCause().getMessage(), containsString("must not be longer than 1"));
    }

    @Test
    void oneFailingValidator() {
        RawMapDto input = new RawMapDto("test", List.of("1"), List.of(), List.of(), List.of(), List.of());

        when(validatorMock1.validate(any(), any())).thenReturn(List.of("message1", "message2"));

        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> underTest.validate(input));
        assertThat(exception.getMessage(), containsString("Validation of map test failed:\nmessage1,\nmessage2"));
    }

    @Test
    void twoFailingValidators() {
        RawMapDto input = new RawMapDto("test", List.of("1"), List.of(), List.of(), List.of(), List.of());

        when(validatorMock1.validate(any(), any())).thenReturn(List.of("validator1_message1", "validator1_message2"));
        when(validatorMock2.validate(any(), any())).thenReturn(List.of("validator2_message1", "validator2_message2"));

        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> underTest.validate(input));
        assertThat(exception.getMessage(), containsString("validator1_message1,\nvalidator1_message2"));
        assertThat(exception.getMessage(), containsString("validator2_message1,\nvalidator2_message2"));
    }
}