package manfred.manfreditor.map;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

class MapModelTest {

    @Test
    void setupWithEmptyMap() {
        MapModel underTest = new MapModel(new Map("name", new HashMap<>()));
    }
}