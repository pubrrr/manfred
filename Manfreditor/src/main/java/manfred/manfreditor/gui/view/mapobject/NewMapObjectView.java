package manfred.manfreditor.gui.view.mapobject;

import lombok.AllArgsConstructor;
import manfred.manfreditor.mapobject.NewMapObjectModel;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NewMapObjectView {

    private final NewMapObjectModel newMapObjectModel;
    private final ImageScaler imageScaler;

    public void draw(GC gc, Display display, Point canvasSize) {
        newMapObjectModel.getImageData()
            .map(imageScaler.scaleToFitIn(canvasSize))
            .ifPresent(imageData -> drawImageAndGrid(gc, display, canvasSize, imageData));
    }

    private void drawImageAndGrid(GC gc, Display display, Point canvasSize, org.eclipse.swt.graphics.ImageData imageData) {
        int imageX = (canvasSize.x - imageData.width) / 2;
        int imageY = (canvasSize.y - imageData.height) / 2;

        gc.drawImage(new Image(display, imageData), imageX, imageY);

        int squareSize = imageData.width;
        gc.drawRectangle(imageX, imageY + imageData.height - squareSize, squareSize, squareSize);
    }
}
