package manfred.manfreditor.map.model;

import manfred.data.shared.PositiveInt;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class MapTest {

    @Test
    void emptyMapSize() {
        Map underTest = new Map("name", new HashMap<>());

        assertThat(underTest.getSizeX(), is(PositiveInt.of(0)));
        assertThat(underTest.getSizeY(), is(PositiveInt.of(0)));
    }
}