package manfred.data.map.tile;

import lombok.Value;
import manfred.data.map.matrix.MapMatrix;

import java.awt.image.BufferedImage;

@Value
public class ValidatedMapTileDto {
    String name;
    MapMatrix<String> structure;
    BufferedImage image;
}
