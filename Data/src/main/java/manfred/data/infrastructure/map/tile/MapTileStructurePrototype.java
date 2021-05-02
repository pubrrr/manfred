package manfred.data.infrastructure.map.tile;

import lombok.Getter;
import manfred.data.infrastructure.map.MapStructurePrototype;
import manfred.data.infrastructure.map.matrix.MapMatrix;

@Getter
public class MapTileStructurePrototype extends MapStructurePrototype {

    public MapTileStructurePrototype(MapMatrix<TilePrototype> map) {
        super(map);
    }
}
