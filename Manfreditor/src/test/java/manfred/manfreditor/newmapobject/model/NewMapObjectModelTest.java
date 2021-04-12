package manfred.manfreditor.newmapobject.model;

import io.vavr.collection.HashMap;
import io.vavr.collection.Seq;
import io.vavr.collection.Set;
import io.vavr.control.Validation;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static manfred.manfreditor.newmapobject.model.NewMapObjectModel.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NewMapObjectModelTest {

    private NewMapObjectModel underTest;
    private ObjectAccessibilityValidator objectAccessibilityValidatorMock;

    @BeforeEach
    void setUp() {
        objectAccessibilityValidatorMock = mock(ObjectAccessibilityValidator.class);
        underTest = new NewMapObjectModel(objectAccessibilityValidatorMock);
    }

    @Test
    void getResultOnNewSessionFails() {
        when(objectAccessibilityValidatorMock.validate(any())).thenReturn(Validation.invalid("accessibility invalid message"));

        Validation<Seq<String>, NewMapObjectData> result = underTest.getResult();

        assertThat(result.isValid(), is(false));
        assertThat(result.getError(), containsInAnyOrder("no name given", "no image data given", "accessibility invalid message"));
    }

    @Test
    void getResultWhenAllDataSetIsSuccessful() {
        ImageData imageData = someImageData();
        AccessibilityGrid accessibilityGridMock = mock(AccessibilityGrid.class);
        when(objectAccessibilityValidatorMock.validate(any())).thenReturn(Validation.valid(accessibilityGridMock));

        underTest.setName("name");
        underTest.setImageData(imageData);
        Validation<Seq<String>, NewMapObjectData> result = underTest.getResult();

        assertThat(result.isValid(), is(true));
        assertThat(result.get().getName(), is("name"));
        assertThat(result.get().getImageData(), is(imageData));
        assertThat(result.get().getAccessibilityGrid(), is(accessibilityGridMock));
    }

    @Test
    void resettingTheSessionRestoresInitialError() {
        AccessibilityGrid accessibilityGridMock = mock(AccessibilityGrid.class);
        when(objectAccessibilityValidatorMock.validate(any())).thenReturn(Validation.valid(accessibilityGridMock));

        ImageData imageData = someImageData();
        Validation<Seq<String>, NewMapObjectData> initialResult = underTest.getResult();
        assertThat(initialResult.isValid(), is(false));
        verify(objectAccessibilityValidatorMock, times(1)).validate(eq(defaultAccessibilityGrid()));

        underTest.setName("name");
        underTest.setImageData(imageData);
        underTest.invertAccessibility(new PreviewTileCoordinate(0, 0));
        Validation<Seq<String>, NewMapObjectData> validResult = underTest.getResult();
        assertThat(validResult.isValid(), is(true));

        underTest.newSession();
        Validation<Seq<String>, NewMapObjectData> resultAfterReset = underTest.getResult();
        assertThat(resultAfterReset.isValid(), is(false));
        assertThat(resultAfterReset.getError(), equalTo(initialResult.getError()));
        verify(objectAccessibilityValidatorMock, times(2)).validate(eq(defaultAccessibilityGrid()));
    }

    @Test
    void setAndGetColumns() {
        assertThat(underTest.getColumns(), is(1));

        underTest.setColumns(5);
        assertThat(underTest.getColumns(), is(5));

        AccessibilityGrid accessibilityGrid = underTest.getAccessibilityGrid();
        Set<PreviewTileCoordinate> accessibilityGridCoordinates = accessibilityGrid.getGrid().keySet();
        assertThat(accessibilityGridCoordinates, containsInAnyOrder(
            new PreviewTileCoordinate(0, 0),
            new PreviewTileCoordinate(1, 0),
            new PreviewTileCoordinate(2, 0),
            new PreviewTileCoordinate(3, 0),
            new PreviewTileCoordinate(4, 0)
        ));

        underTest.setColumns(3);
        assertThat(underTest.getColumns(), is(3));

        AccessibilityGrid accessibilityGrid2 = underTest.getAccessibilityGrid();
        Set<PreviewTileCoordinate> accessibilityGridCoordinates2 = accessibilityGrid2.getGrid().keySet();
        assertThat(accessibilityGridCoordinates2, containsInAnyOrder(
            new PreviewTileCoordinate(0, 0),
            new PreviewTileCoordinate(1, 0),
            new PreviewTileCoordinate(2, 0)
        ));
    }

    @Test
    void setAndGetRows() {
        assertThat(underTest.getRows(), is(1));

        underTest.setRows(5);
        assertThat(underTest.getRows(), is(5));

        AccessibilityGrid accessibilityGrid = underTest.getAccessibilityGrid();
        Set<PreviewTileCoordinate> accessibilityGridCoordinates = accessibilityGrid.getGrid().keySet();
        assertThat(accessibilityGridCoordinates, containsInAnyOrder(
            new PreviewTileCoordinate(0, 0),
            new PreviewTileCoordinate(0, 1),
            new PreviewTileCoordinate(0, 2),
            new PreviewTileCoordinate(0, 3),
            new PreviewTileCoordinate(0, 4)
        ));

        underTest.setRows(3);
        assertThat(underTest.getRows(), is(3));

        AccessibilityGrid accessibilityGrid2 = underTest.getAccessibilityGrid();
        Set<PreviewTileCoordinate> accessibilityGridCoordinates2 = accessibilityGrid2.getGrid().keySet();
        assertThat(accessibilityGridCoordinates2, containsInAnyOrder(
            new PreviewTileCoordinate(0, 0),
            new PreviewTileCoordinate(0, 1),
            new PreviewTileCoordinate(0, 2)
        ));
    }

    @Test
    void setRowsAndColumns() {
        underTest.setColumns(4);
        underTest.setRows(2);

        assertThat(underTest.getColumns(), is(4));
        assertThat(underTest.getRows(), is(2));

        AccessibilityGrid accessibilityGrid = underTest.getAccessibilityGrid();
        Set<PreviewTileCoordinate> accessibilityGridCoordinates = accessibilityGrid.getGrid().keySet();
        assertThat(accessibilityGridCoordinates, containsInAnyOrder(
            new PreviewTileCoordinate(0, 0),
            new PreviewTileCoordinate(1, 0),
            new PreviewTileCoordinate(2, 0),
            new PreviewTileCoordinate(3, 0),
            new PreviewTileCoordinate(0, 1),
            new PreviewTileCoordinate(1, 1),
            new PreviewTileCoordinate(2, 1),
            new PreviewTileCoordinate(3, 1)
        ));

        underTest.setColumns(3);
        underTest.setRows(1);

        assertThat(underTest.getColumns(), is(3));
        assertThat(underTest.getRows(), is(1));

        AccessibilityGrid accessibilityGrid2 = underTest.getAccessibilityGrid();
        Set<PreviewTileCoordinate> accessibilityGridCoordinates2 = accessibilityGrid2.getGrid().keySet();
        assertThat(accessibilityGridCoordinates2, containsInAnyOrder(
            new PreviewTileCoordinate(0, 0),
            new PreviewTileCoordinate(1, 0),
            new PreviewTileCoordinate(2, 0)
        ));
    }

    private AccessibilityGrid defaultAccessibilityGrid() {
        return new AccessibilityGrid(HashMap.of(new PreviewTileCoordinate(0, 0), true));
    }

    private ImageData someImageData() {
        return new ImageData(1, 1, 1, new PaletteData(0, 0, 0));
    }
}