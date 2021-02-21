package manfred.data.infrastructure.map.tile;

import lombok.Value;
import manfred.data.infrastructure.map.matrix.MapMatrix;

import java.awt.image.BufferedImage;

@Value
public class ValidatedMapTileDto {
    String name;
    MapMatrix<TilePrototype> structure;
    BufferedImage image;
}
