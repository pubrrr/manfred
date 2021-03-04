package manfred.manfreditor.mapobject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class EmptySelectionTest {

    private EmptySelection underTest;

    @BeforeEach
    void setUp() {
        underTest = new EmptySelection();
    }

    @Test
    void select() {
        MapObjectRepository.ObjectKey selectedKeyMock = mock(MapObjectRepository.ObjectKey.class);

        SelectionState result = underTest.select(selectedKeyMock);

        assertThat(result, instanceOf(KeySelection.class));
        assertThat(result.getSelection(), is(Optional.of(selectedKeyMock)));
    }

    @Test
    void getSelectionIsAlwaysEmpty() {
        Optional<MapObjectRepository.ObjectKey> selection = underTest.getSelection();

        assertTrue(selection.isEmpty());
    }
}