package manfred.data.map;

import manfred.data.InvalidInputException;
import manfred.data.map.validator.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MapDtoValidatorTest {

    private MapDtoValidator underTest;

    private Validator validatorMock1;

    private Validator validatorMock2;

    @BeforeEach
    void setUp() {
        validatorMock1 = mock(Validator.class);
        validatorMock2 = mock(Validator.class);
        underTest = new MapDtoValidator(List.of(validatorMock1, validatorMock2));
    }

    @Test
    void emptyInput() throws InvalidInputException {
        RawMapDto input = new RawMapDto("test", List.of(), List.of(), List.of(), List.of(), List.of());

        ValidatedMapDto result = underTest.validate(input);

        assertThat(result.getMap().getMatrix(), empty());
        assertThat(result.getPersons(), empty());
        assertThat(result.getPortals(), empty());
        assertThat(result.getDoors(), empty());
        assertThat(result.getEnemies(), empty());
    }

    @Test
    void emptyEmptyButOtherStructs() throws InvalidInputException {
        RawMapDto input = new RawMapDto(
            "test",
            List.of(),
            List.of(new PersonDto()),
            List.of(new TransporterDto()),
            List.of(new TransporterDto(), new TransporterDto()),
            List.of(new EnemyDto())
        );

        ValidatedMapDto result = underTest.validate(input);

        assertThat(result.getMap().getMatrix(), empty());
        assertThat(result.getPersons(), hasSize(1));
        assertThat(result.getPortals(), hasSize(1));
        assertThat(result.getDoors(), hasSize(2));
        assertThat(result.getEnemies(), hasSize(1));
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

        ValidatedMapDto result = underTest.validate(input);

        List<List<String>> matrix = result.getMap().getMatrix();
        assertThat(matrix.size(), is(1));
        assertThat(matrix.get(0).size(), is(2));
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

        ValidatedMapDto result = underTest.validate(input);

        List<List<String>> matrix = result.getMap().getMatrix();
        assertThat(matrix.size(), is(2));
        assertThat(matrix.get(0).size(), is(1));
    }

    @Test
    void twoXTwoMap() throws InvalidInputException {
        RawMapDto input = new RawMapDto(
            "test",
            List.of(
                "0,1",
                "2,3"
            ),
            List.of(),
            List.of(),
            List.of(),
            List.of()
        );

        ValidatedMapDto result = underTest.validate(input);

        List<List<String>> matrix = result.getMap().getMatrix();
        assertThat(matrix.size(), is(2));
        assertThat(matrix.get(0).size(), is(2));
        assertThat(matrix.get(0).get(1), is("2"));
        assertThat(matrix.get(1).get(0), is("1"));
    }

    @Test
    void twoXTwoMap_withStringToTrim() throws InvalidInputException {
        RawMapDto input = new RawMapDto(
            "test",
            List.of(
                "0 ,1",
                "2, 3 "
            ),
            List.of(),
            List.of(),
            List.of(),
            List.of()
        );

        ValidatedMapDto result = underTest.validate(input);

        List<List<String>> matrix = result.getMap().getMatrix();
        assertThat(matrix.size(), is(2));
        assertThat(matrix.get(0).size(), is(2));
        assertThat(matrix.get(0).get(1), is("2"));
        assertThat(matrix.get(1).get(0), is("1"));
    }

    @Test
    void twoRowsThreeColumnsMap() throws InvalidInputException {
        RawMapDto input = new RawMapDto(
            "test",
            List.of(
                "0,1,2",
                "3,4,5"
            ),
            List.of(),
            List.of(),
            List.of(),
            List.of()
        );

        ValidatedMapDto result = underTest.validate(input);

        List<List<String>> matrix = result.getMap().getMatrix();
        assertThat(matrix.size(), is(3));
        assertThat(matrix.get(0).size(), is(2));
        assertThat(matrix.get(0).get(1), is("3"));
        assertThat(matrix.get(2).get(1), is("5"));
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
        RawMapDto input = new RawMapDto("test", List.of(), List.of(), List.of(), List.of(), List.of());

        when(validatorMock1.validate(any())).thenReturn(List.of("message1", "message2"));

        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> underTest.validate(input));
        assertThat(exception.getMessage(), containsString("Validation of map test failed:\nmessage1,\nmessage2"));
    }

    @Test
    void twoFailingValidators() {
        RawMapDto input = new RawMapDto("test", List.of(), List.of(), List.of(), List.of(), List.of());

        when(validatorMock1.validate(any())).thenReturn(List.of("validator1_message1", "validator1_message2"));
        when(validatorMock2.validate(any())).thenReturn(List.of("validator2_message1", "validator2_message2"));

        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> underTest.validate(input));
        assertThat(exception.getMessage(), containsString("validator1_message1,\nvalidator1_message2"));
        assertThat(exception.getMessage(), containsString("validator2_message1,\nvalidator2_message2"));
    }
}