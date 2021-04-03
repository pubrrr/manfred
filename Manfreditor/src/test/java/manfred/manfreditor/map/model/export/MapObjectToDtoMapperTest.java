package manfred.manfreditor.map.model.export;

import io.vavr.collection.HashMap;
import manfred.data.persistence.dto.RawMapTileDto;
import manfred.manfreditor.newmapobject.model.AccessibilityGrid;
import manfred.manfreditor.newmapobject.model.NewMapObjectData;
import manfred.manfreditor.newmapobject.model.NewMapObjectModel.PreviewTileCoordinate;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

class MapObjectToDtoMapperTest {

    private MapObjectToDtoMapper underTest;

    @BeforeEach
    void setUp() {
        underTest = new MapObjectToDtoMapper();
    }

    @Test
    void mapWithOneAccessibleTile() {
        AccessibilityGrid accessibilityGrid = new AccessibilityGrid(HashMap.of(previewTileCoordinate(0, 0), true));
        ImageData imageData = someImageData();
        var input = new NewMapObjectData("name", imageData, accessibilityGrid);

        RawMapTileDto result = underTest.map(input);

        assertThat(result.getName(), is("name"));
        assertThat(result.getImageData(), is(imageData));
        assertThat(result.getStructure(), hasSize(1));
        assertThat(result.getStructure(), hasItem("1"));
    }

    @Test
    void mapWithOneInaccessibleTile() {
        AccessibilityGrid accessibilityGrid = new AccessibilityGrid(HashMap.of(previewTileCoordinate(0, 0), false));
        ImageData imageData = someImageData();
        var input = new NewMapObjectData("name", imageData, accessibilityGrid);

        RawMapTileDto result = underTest.map(input);

        assertThat(result.getName(), is("name"));
        assertThat(result.getImageData(), is(imageData));
        assertThat(result.getStructure(), hasSize(1));
        assertThat(result.getStructure(), hasItem("0"));
    }

    @Test
    void mapWithTilesInARow() {
        AccessibilityGrid accessibilityGrid = new AccessibilityGrid(HashMap.of(
            previewTileCoordinate(1, 0), false,
            previewTileCoordinate(0, 0), true
        ));
        ImageData imageData = someImageData();
        var input = new NewMapObjectData("name", imageData, accessibilityGrid);

        RawMapTileDto result = underTest.map(input);

        assertThat(result.getName(), is("name"));
        assertThat(result.getImageData(), is(imageData));
        assertThat(result.getStructure(), hasSize(1));
        assertThat(result.getStructure(), hasItem("1,0"));
    }

    @Test
    void mapWithTilesInAColumn() {
        AccessibilityGrid accessibilityGrid = new AccessibilityGrid(HashMap.of(
            previewTileCoordinate(0, 1), false,
            previewTileCoordinate(0, 0), true
        ));
        ImageData imageData = someImageData();
        var input = new NewMapObjectData("name", imageData, accessibilityGrid);

        RawMapTileDto result = underTest.map(input);

        assertThat(result.getName(), is("name"));
        assertThat(result.getImageData(), is(imageData));
        assertThat(result.getStructure(), hasSize(2));
        assertThat(result.getStructure(), contains("0", "1"));
    }

    @Test
    void map2x2Tiles() {
        AccessibilityGrid accessibilityGrid = new AccessibilityGrid(HashMap.of(
            previewTileCoordinate(1, 1), false,
            previewTileCoordinate(1, 0), true,
            previewTileCoordinate(0, 1), false,
            previewTileCoordinate(0, 0), true
        ));
        ImageData imageData = someImageData();
        var input = new NewMapObjectData("name", imageData, accessibilityGrid);

        RawMapTileDto result = underTest.map(input);

        assertThat(result.getName(), is("name"));
        assertThat(result.getImageData(), is(imageData));
        assertThat(result.getStructure(), hasSize(2));
        assertThat(result.getStructure(), contains("0,0", "1,1"));
    }

    private PreviewTileCoordinate previewTileCoordinate(int x, int y) {
        return new PreviewTileCoordinate(x, y);
    }

    private ImageData someImageData() {
        return new ImageData(1, 1, 1, new PaletteData(1, 1, 1));
    }
}