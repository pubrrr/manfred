package manfred.manfreditor.map.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MapRepositoryTest {

    private MapRepository underTest;

    @BeforeEach
    void setup() {
        underTest = new MapRepository();
    }

    @Test
    void initiallyEmpty() {
        assertThat(underTest.getKeys().toJavaList(), empty());
    }

    @Test
    void populateAndGet() {
        Map mapMock = mock(Map.class);
        when(mapMock.getName()).thenReturn("name");

        MapRepository.MapKey mapKey = underTest.populateWith(mapMock);
        assertThat(mapKey.value, is("name"));

        assertThat(underTest.get(mapKey), is(mapMock));
        assertThat(underTest.getKeys().size(), is(1));
        assertThat(underTest.getKeys().head(), is(mapKey));
    }

    @Test
    void overrideMap() {
        Map mapMock1 = mock(Map.class);
        when(mapMock1.getName()).thenReturn("name");
        Map mapMock2 = mock(Map.class);
        when(mapMock2.getName()).thenReturn("name");

        MapRepository.MapKey mapKey1 = underTest.populateWith(mapMock1);
        assertThat(mapKey1.value, is("name"));
        assertThat(underTest.getKeys().size(), is(1));

        MapRepository.MapKey mapKey2 = underTest.populateWith(mapMock2);
        assertThat(mapKey2.value, is("name"));
        assertThat(underTest.getKeys().size(), is(1));

        assertThat(underTest.get(mapKey1), is(mapMock2));
    }

    @Test
    void populateTwice() {
        Map mapMock1 = mock(Map.class);
        when(mapMock1.getName()).thenReturn("name1");
        Map mapMock2 = mock(Map.class);
        when(mapMock2.getName()).thenReturn("name2");

        underTest.populateWith(mapMock1);
        underTest.populateWith(mapMock2);

        assertThat(underTest.getKeys().size(), is(2));
    }
}
