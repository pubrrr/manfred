package manfred.manfreditor.gui.view.mapobject;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ImageScaler {

    public Function<ImageData, ImageData> scaleToFitIn(Point canvasSize) {
        return imageData -> {
            double scaleFactor = Math.min(
                (double) canvasSize.x / imageData.width,
                (double) canvasSize.y / imageData.height
            );
            return imageData.scaledTo((int) (scaleFactor * imageData.width), (int) (scaleFactor * imageData.height));
        };
    }
}
