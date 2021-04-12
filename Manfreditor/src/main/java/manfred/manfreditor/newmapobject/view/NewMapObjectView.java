package manfred.manfreditor.newmapobject.view;

import io.vavr.Tuple2;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Value;
import manfred.manfreditor.newmapobject.model.NewMapObjectModel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.function.Predicate;

import static manfred.manfreditor.newmapobject.model.NewMapObjectModel.*;

@Component
@AllArgsConstructor
public class NewMapObjectView {

    private final static RGB red = new RGB(255, 0, 0);

    private final NewMapObjectModel newMapObjectModel;
    private final ImageScaler imageScaler;

    public void draw(GC gc, Display display, Point canvasSize) {
        newMapObjectModel.getImageData()
            .map(imageScaler.scaleToFitIn(canvasSize))
            .ifPresent(imageData -> drawImageAndGrid(gc, display, canvasSize, imageData));
    }

    private void drawImageAndGrid(GC gc, Display display, Point canvasSize, org.eclipse.swt.graphics.ImageData imageData) {
        int gridTileSize = getGridTileSize(imageData);
        Point imagePosition = getImagePosition(imageData, gridTileSize, canvasSize);

        gc.drawImage(new Image(display, imageData), imagePosition.x, imagePosition.y);

        int gridTopCoordinate = getGridTopCoordinate(imageData, gridTileSize, imagePosition);
        gc.setBackground(new Color(display, red));
        gc.setAlpha(120);
        newMapObjectModel.getAccessibilityGrid().getGrid()
            .mapKeys(toCoordinateOnCanvas(gridTileSize, imagePosition.x, gridTopCoordinate))
            .forEach((viewCoordinate, isAccessible) -> {
                gc.drawRectangle(viewCoordinate.getX(), viewCoordinate.getY(), gridTileSize, gridTileSize);
                if (!isAccessible) {
                    gc.fillRectangle(viewCoordinate.getX(), viewCoordinate.getY(), gridTileSize, gridTileSize);
                }
            });
    }

    private Function<PreviewTileCoordinate, PreviewViewCoordinate> toCoordinateOnCanvas(
        int gridTileSize,
        int gridLeftCoordinate,
        int gridTopCoordinate
    ) {
        int gridSizeY = newMapObjectModel.getAccessibilityGrid().getSizeY();

        return previewTileCoordinate -> new PreviewViewCoordinate(
            previewTileCoordinate.x() * gridTileSize + gridLeftCoordinate,
            (gridSizeY - previewTileCoordinate.y() - 1) * gridTileSize + gridTopCoordinate
        );
    }

    public Option<PreviewTileCoordinate> getClickedTile(int x, int y, Point canvasSize) {
        return Option.ofOptional(newMapObjectModel.getImageData().map(imageScaler.scaleToFitIn(canvasSize)))
            .flatMap(imageData -> getClickedTileOnImage(imageData, x, y, canvasSize));
    }

    private Option<PreviewTileCoordinate> getClickedTileOnImage(ImageData imageData, int x, int y, Point canvasSize) {
        int gridTileSize = getGridTileSize(imageData);
        Point imagePosition = getImagePosition(imageData, gridTileSize, canvasSize);

        int gridTopCoordinate = getGridTopCoordinate(imageData, gridTileSize, imagePosition);
        return newMapObjectModel.getAccessibilityGrid()
            .getGrid()
            .keySet()
            .toMap(tileCoordinate -> tileCoordinate, toCoordinateOnCanvas(gridTileSize, imagePosition.x, gridTopCoordinate))
            .find(tileWithSizeContains(gridTileSize, x, y))
            .map(Tuple2::_1);
    }

    private Predicate<Tuple2<PreviewTileCoordinate, PreviewViewCoordinate>> tileWithSizeContains(int gridTileSize, int x, int y) {
        return coordinateTuple -> {
            PreviewViewCoordinate previewViewCoordinate = coordinateTuple._2;
            return x >= previewViewCoordinate.x
                && x < previewViewCoordinate.x + gridTileSize
                && y >= previewViewCoordinate.y
                && y < previewViewCoordinate.y + gridTileSize;
        };
    }

    private int getGridTileSize(ImageData imageData) {
        return imageData.width / newMapObjectModel.getAccessibilityGrid().getSizeX();
    }

    private Point getImagePosition(ImageData imageData, int gridTileSize, Point canvasSize) {
        return new Point(
            (canvasSize.x - imageData.width) / 2,
            Math.max((canvasSize.y - imageData.height) / 2, gridTileSize - imageData.height)
        );
    }

    private int getGridTopCoordinate(ImageData imageData, int gridTileSize, Point imagePosition) {
        int gridSizeY = newMapObjectModel.getAccessibilityGrid().getSizeY();
        return imagePosition.y + imageData.height - gridSizeY * gridTileSize;
    }

    @Value
    private static class PreviewViewCoordinate {
        int x;
        int y;
    }
}
