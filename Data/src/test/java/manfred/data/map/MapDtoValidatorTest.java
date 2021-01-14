package manfred.data.map;

import manfred.data.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;

class MapDtoValidatorTest {

    private MapDtoValidator underTest;

    @BeforeEach
    void setUp() {
        underTest = new MapDtoValidator();
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
}