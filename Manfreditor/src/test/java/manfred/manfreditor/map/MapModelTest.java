package manfred.manfreditor.map;

import manfred.manfreditor.map.accessibility.AccessibilityMerger;
import manfred.manfreditor.map.accessibility.ColoredAccessibilityIndicator;
import manfred.manfreditor.map.accessibility.EmptyAccessibilityIndicator;
import manfred.manfreditor.map.accessibility.Source;
import manfred.manfreditor.mapobject.ConcreteMapObject;
import manfred.manfreditor.mapobject.MapObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static manfred.manfreditor.helper.CoordinateHelper.tileCoordinate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MapModelTest {

    private MapModel underTest;
    private Map mapMock;
    private AccessibilityMerger accessibilityMergerMock;
    private ObjectInsertionValidator objectInsertionValidator;

    @BeforeEach
    void setUp() {
        mapMock = mock(Map.class);
        accessibilityMergerMock = mock(AccessibilityMerger.class);
        objectInsertionValidator = mock(ObjectInsertionValidator.class);
        underTest = new MapModel(mapMock, accessibilityMergerMock, objectInsertionValidator);
    }

    @Test
    void givenInsertionValidationIsSuccessful_thenObjectIsInserted() {
        when(accessibilityMergerMock.merge(any())).thenReturn(new HashMap<>());
        when(objectInsertionValidator.mayObjectBeInserted(any(), any(), any())).thenReturn(ObjectInsertionValidator.Result.success());

        ConcreteMapObject objectToInsert = mock(ConcreteMapObject.class);
        Map.TileCoordinate coordinateToInsertAt = mock(Map.TileCoordinate.class);
        List<String> result = underTest.tryInsertObjectAt(objectToInsert, coordinateToInsertAt);

        verify(mapMock).insertObjectAt(eq(objectToInsert), eq(coordinateToInsertAt));
        assertThat(result, empty());
    }

    @Test
    void givenInsertionValidationFails_thenObjectIsNotInserted() {
        List<String> validationMessages = List.of("message1", "message2");
        when(accessibilityMergerMock.merge(any())).thenReturn(new HashMap<>());
        when(objectInsertionValidator.mayObjectBeInserted(any(), any(), any())).thenReturn(
            ObjectInsertionValidator.Result.failedWithMessages(validationMessages)
        );

        List<String> result = underTest.tryInsertObjectAt(mock(ConcreteMapObject.class), mock(Map.TileCoordinate.class));

        verify(mapMock, never()).insertObjectAt(any(), any());
        assertThat(result, is(validationMessages));
    }

    @Test
    void deleteEmptyObject_doesNothing() {
        when(accessibilityMergerMock.merge(any())).thenReturn(java.util.Map.of(
            tileCoordinate(0, 0), new EmptyAccessibilityIndicator()
        ));

        underTest.deleteObjectAt(tileCoordinate(0, 0));

        verify(mapMock, never()).insertObjectAt(any(), any());
    }

    @Test
    void deleteNonEmptyObject_thenEmptyObjectIsInsertedInMap() {
        var sourceTileCoordinate = tileCoordinate(1, 2);
        var nonEmptyObject = new ColoredAccessibilityIndicator(null, new Source("name", sourceTileCoordinate));
        when(accessibilityMergerMock.merge(any())).thenReturn(java.util.Map.of(tileCoordinate(0, 0), nonEmptyObject));

        underTest.deleteObjectAt(tileCoordinate(0, 0));

        verify(mapMock, never()).insertObjectAt(eq(MapObject.none()), eq(sourceTileCoordinate));
    }
}