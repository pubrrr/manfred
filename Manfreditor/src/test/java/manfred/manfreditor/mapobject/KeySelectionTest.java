package manfred.manfreditor.mapobject;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

class KeySelectionTest {

    @Test
    void getSelection() {
        MapObjectRepository.ObjectKey selectedKeyMock = mock(MapObjectRepository.ObjectKey.class);
        KeySelection underTest = new KeySelection(selectedKeyMock);

        Optional<MapObjectRepository.ObjectKey> result = underTest.getSelection();

        assertThat(result, is(Optional.of(selectedKeyMock)));
    }

    @Test
    void selectAlreadySelectedKey_thenKeyIsDeselected() {
        MapObjectRepository.ObjectKey selecteKeyMock = mock(MapObjectRepository.ObjectKey.class);
        KeySelection underTest = new KeySelection(selecteKeyMock);

        SelectionState result = underTest.select(selecteKeyMock);

        assertThat(result, instanceOf(EmptySelection.class));
    }

    @Test
    void selecteAnotherKey_thenThatKeyIsSelected() {
        MapObjectRepository.ObjectKey firstSelectionMock = mock(MapObjectRepository.ObjectKey.class);
        KeySelection underTest = new KeySelection(firstSelectionMock);
        assertThat(underTest.getSelection(), is(Optional.of(firstSelectionMock)));

        MapObjectRepository.ObjectKey secondSelectionMock = mock(MapObjectRepository.ObjectKey.class);
        SelectionState newState = underTest.select(secondSelectionMock);

        assertThat(newState.getSelection(), is(Optional.of(secondSelectionMock)));
    }
}