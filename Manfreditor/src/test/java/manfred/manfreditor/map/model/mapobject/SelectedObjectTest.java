package manfred.manfreditor.map.model.mapobject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SelectedObjectTest {

    private SelectedObject underTest;
    private SelectionState initialSelectionMock;

    @BeforeEach
    void setUp() {
        initialSelectionMock = mock(SelectionState.class);
        underTest = new SelectedObject(initialSelectionMock);
    }

    @Test
    void select() {
        SelectionState newSelectionStateMock = mock(SelectionState.class);
        MapObjectRepository.ObjectKey newSelectedKeyMock = mock(MapObjectRepository.ObjectKey.class);
        when(initialSelectionMock.select(any())).thenReturn(newSelectionStateMock);
        when(newSelectionStateMock.getSelection()).thenReturn(Optional.of(newSelectedKeyMock));

        underTest.select(mock(MapObjectRepository.ObjectKey.class));
        Optional<MapObjectRepository.ObjectKey> result = underTest.getSelection();

        assertThat(result, is(Optional.of(newSelectedKeyMock)));
    }
}