package manfred.data.map.matrix;

import manfred.data.InvalidInputException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MapMatrixTest {

    @Test
    void emptyInput() {
        InvalidInputException exception = assertThrows(
            InvalidInputException.class,
            () -> MapMatrix.fromRawDtoData(List.of()).validateAndBuild()
        );
        assertThat(exception.getMessage(), containsString("Map matrix must not be empty"));
    }

    @Test
    void emptyInput2() {
        InvalidInputException exception = assertThrows(
            InvalidInputException.class,
            () -> MapMatrix.fromRawDtoData(List.of(List.of())).validateAndBuild()
        );
        assertThat(exception.getMessage(), containsString("Map matrix must not be empty"));
    }
}