package manfred.data.map.validator;

import manfred.data.map.RawMapDto;
import manfred.data.map.matrix.MapMatrix;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.mockito.Mockito.mock;

class PersonsValidatorTest {

    private PersonsValidator underTest;

    @BeforeEach
    void setUp() {
        underTest = new PersonsValidator();
    }

    @Test
    void emptyPerson() {
        List<String> result = underTest.validate(new RawMapDto(), mock(MapMatrix.class));

        assertThat(result, empty());
    }
}