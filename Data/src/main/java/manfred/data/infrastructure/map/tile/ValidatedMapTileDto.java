package manfred.data.infrastructure.map.tile;

import lombok.Value;
import manfred.data.infrastructure.map.MapPrototype;
import org.eclipse.swt.graphics.ImageData;

import java.awt.image.BufferedImage;

@Value
public class ValidatedMapTileDto {
    String name;
    MapPrototype structure;
    BufferedImage image;
    ImageData imageData;
}
