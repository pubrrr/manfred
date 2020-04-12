package manfred.game.interact;

import manfred.game.exception.InvalidInputException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonReaderTest {
    private static PersonReader underTest;

    @BeforeAll
    static void init() {
        underTest = new PersonReader();
    }

    @Test
    void convertsName() throws InvalidInputException {
        Person result = underTest.convert("{\"name\":\"testName\"}");

        assertEquals("testName", result.getName());
    }
}