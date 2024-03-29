package manfred.manfreditor.newmapobject.model;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.collection.Stream;
import io.vavr.control.Validation;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.eclipse.swt.graphics.ImageData;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class NewMapObjectModel {

    private final ObjectAccessibilityValidator objectAccessibilityValidator;

    private String name;
    private ImageData imageData;
    private Map<PreviewTileCoordinate, Boolean> accessibilityGrid = HashMap.of(new PreviewTileCoordinate(0, 0), true);

    public void newSession() {
        this.name = null;
        this.imageData = null;
        this.accessibilityGrid = HashMap.of(new PreviewTileCoordinate(0, 0), true);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageData(ImageData imageData) {
        this.imageData = imageData;
    }

    public Optional<ImageData> getImageData() {
        return Optional.ofNullable(imageData);
    }

    public AccessibilityGrid getAccessibilityGrid() {
        return new AccessibilityGrid(accessibilityGrid);
    }

    public Validation<Seq<String>, NewMapObjectData> getResult() {
        return Validation
            .combine(
                name == null || name.isEmpty() ? Validation.invalid("no name given") : Validation.valid(this.name),
                imageData == null ? Validation.invalid("no image data given") : Validation.valid(this.imageData),
                objectAccessibilityValidator.validate(getAccessibilityGrid())
            )
            .ap(NewMapObjectData::new);
    }

    public void invertAccessibility(PreviewTileCoordinate previewTileCoordinate) {
        if (this.accessibilityGrid.containsKey(previewTileCoordinate)) {
            this.accessibilityGrid = this.accessibilityGrid.put(
                previewTileCoordinate,
                !this.accessibilityGrid.get(previewTileCoordinate).get()
            );
        }
    }

    public int getColumns() {
        return this.getAccessibilityGrid().getSizeX();
    }

    public void setColumns(int columns) {
        int currentColumns = getColumns();
        if (columns <= currentColumns) {
            this.accessibilityGrid = this.accessibilityGrid.filterKeys(tileCoordinate -> tileCoordinate.x < columns);
        } else {
            this.accessibilityGrid = this.accessibilityGrid.merge(
                Stream.range(currentColumns, columns)
                    .crossProduct(getCoordinateRange(PreviewTileCoordinate::y))
                    .toMap(xAndYToNewAccessibleTile())
            );
        }
    }

    public int getRows() {
        return this.getAccessibilityGrid().getSizeY();
    }

    public void setRows(int rows) {
        int currentRows = getRows();
        if (rows <= currentRows) {
            this.accessibilityGrid = this.accessibilityGrid.filterKeys(tileCoordinate -> tileCoordinate.y < rows);
        } else {
            this.accessibilityGrid = this.accessibilityGrid.merge(
                getCoordinateRange(PreviewTileCoordinate::x)
                    .crossProduct(Stream.range(currentRows, rows))
                    .toMap(xAndYToNewAccessibleTile())
            );
        }
    }

    private List<Integer> getCoordinateRange(Function<PreviewTileCoordinate, Integer> coordinateToValue) {
        return accessibilityGrid.keySet().map(coordinateToValue).distinct().toList();
    }

    private Function<Tuple2<Integer, Integer>, Tuple2<PreviewTileCoordinate, Boolean>> xAndYToNewAccessibleTile() {
        return xAndY -> Tuple.of(new PreviewTileCoordinate(xAndY._1, xAndY._2), true);
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    public static class PreviewTileCoordinate {
        private final int x;
        private final int y;

        public int x() {
            return x;
        }

        public int y() {
            return y;
        }
    }
}
