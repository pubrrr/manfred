package manfred.data.persistence.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import manfred.data.InvalidInputException;
import manfred.data.persistence.dto.AttackDto;
import manfred.data.shared.PositiveInt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

class AttackReaderTest {
    private AttackReader underTest;

    @BeforeEach
    void init() {
        underTest = new AttackReader(new ObjectMapper(new YAMLFactory()), mock(ImageLoader.class));
    }

    @Test
    void convert() throws InvalidInputException {
        AttackDto result = underTest.load("test", getClass().getResource("/attack/testThunder.yaml"));

        assertThat(result.getName(), equalTo("thunder"));
        assertThat(result.getSpeed(), equalTo(PositiveInt.of(5)));
        assertThat(result.getSizeX(), equalTo(PositiveInt.of(60)));
        assertThat(result.getSizeY(), equalTo(PositiveInt.of(60)));
        assertThat(result.getRange(), equalTo(PositiveInt.of(320)));
        assertThat(result.getNumberOfAnimationImages(), equalTo(PositiveInt.of(5)));
        assertThat(result.getAttackAnimation(), not(empty()));
    }

    @Test
    void unknownAttack() {
        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> underTest.load("unknown"));
        assertThat(exception.getMessage(), containsString("Did not find resource for attack"));
    }
}