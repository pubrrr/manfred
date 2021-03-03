package manfred.data.infrastructure.map.tile;

import org.springframework.stereotype.Component;

@Component
public class AccessibilityTileConverter extends TileConverter {

    @Override
    public TilePrototype stringToObject(String tileValue) {
        return isNotAccessible(tileValue)
            ? TilePrototype.notAccessible()
            : TilePrototype.accessible();
    }
}
