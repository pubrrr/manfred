package manfred.manfreditor.map;

import io.vavr.collection.HashMap;
import io.vavr.control.Validation;
import manfred.manfreditor.common.Memento;
import manfred.manfreditor.map.accessibility.AccessibilityMerger;
import manfred.manfreditor.map.accessibility.ColoredAccessibilityIndicator;
import manfred.manfreditor.map.accessibility.EmptyAccessibilityIndicator;
import manfred.manfreditor.map.accessibility.Source;
import manfred.manfreditor.mapobject.ConcreteMapObject;
import manfred.manfreditor.mapobject.MapObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static manfred.manfreditor.helper.CoordinateHelper.tileCoordinate;
import static org.hamcrest.MatcherAssert.assertThat;
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
        when(accessibilityMergerMock.merge(any())).thenReturn(HashMap.empty());
        when(objectInsertionValidator.mayObjectBeInserted(any(), any(), any())).thenReturn(Validation.valid(null));

        ConcreteMapObject objectToInsert = mock(ConcreteMapObject.class);
        Map.TileCoordinate coordinateToInsertAt = mock(Map.TileCoordinate.class);
        Validation<List<String>, ConcreteMapObject> result = underTest.tryInsertObjectAt(objectToInsert, coordinateToInsertAt);

        verify(mapMock).insertObjectAt(eq(objectToInsert), eq(coordinateToInsertAt));
        assertThat(result.isValid(), is(true));
    }

    @Test
    void givenInsertionValidationFails_thenObjectIsNotInserted() {
        List<String> validationMessages = List.of("message1", "message2");
        when(accessibilityMergerMock.merge(any())).thenReturn(HashMap.empty());
        when(objectInsertionValidator.mayObjectBeInserted(any(), any(), any())).thenReturn(
            Validation.invalid(validationMessages)
        );

        Validation<List<String>, ConcreteMapObject> result = underTest.tryInsertObjectAt(mock(ConcreteMapObject.class), mock(Map.TileCoordinate.class));

        verify(mapMock, never()).insertObjectAt(any(), any());
        assertThat(result.getError(), is(validationMessages));
    }

    @Test
    void deleteEmptyObject_insertsNewEmptyObjectAndNoDeletedObjectIsReturned() {
        when(this.mapMock.getObjectAt(any())).thenReturn(MapObject.none());
        when(accessibilityMergerMock.merge(any())).thenReturn(HashMap.of(
            tileCoordinate(0, 0), new EmptyAccessibilityIndicator()
        ));

        Map.TileCoordinate tileToDelete = tileCoordinate(0, 0);
        Optional<LocatedMapObject> result = underTest.deleteObjectAt(tileToDelete);

        assertThat(result, is(Optional.empty()));
        verify(mapMock).insertObjectAt(eq(MapObject.none()), eq(tileToDelete));
    }

    @Test
    void deleteNonEmptyObject_thenEmptyObjectIsInsertedInMap() {
        ConcreteMapObject deletedObject = mock(ConcreteMapObject.class);
        when(this.mapMock.getObjectAt(any())).thenReturn(deletedObject);

        var sourceTileCoordinate = tileCoordinate(1, 2);
        var nonEmptyObject = new ColoredAccessibilityIndicator(null, new Source("name", sourceTileCoordinate));
        when(accessibilityMergerMock.merge(any())).thenReturn(HashMap.of(tileCoordinate(0, 0), nonEmptyObject));

        Optional<LocatedMapObject> result = underTest.deleteObjectAt(tileCoordinate(0, 0));

        assertThat(result, is(Optional.of(new LocatedMapObject(deletedObject, sourceTileCoordinate))));
        verify(mapMock).insertObjectAt(eq(MapObject.none()), eq(sourceTileCoordinate));
    }

    @Test
    void takeBackupAndRestoreIt_thenMapIsOriginalMapAgain() {
        Memento<MapModel> backup = underTest.backup();

        Map newMapMock = mock(Map.class);
        underTest.setMap(newMapMock);
        underTest.getObjects();
        verify(this.mapMock, never()).getObjects();
        verify(newMapMock).getObjects();

        backup.restoreStateOf(this.underTest);
        underTest.getObjects();
        verify(this.mapMock).getObjects();
    }
}