package manfred.infrastructureadapter.map.tile;

import lombok.AllArgsConstructor;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.game.config.GameConfig;
import manfred.game.map.DebugTileWrapper;
import manfred.game.map.MapTile;

import java.util.Optional;

@AllArgsConstructor
public class DebugWrapperFactory implements TileConversionRule {

    private final TileConversionRule wrapped;
    private final GameConfig gameConfig;

    @Override
    public Optional<TileConversionAction> applicableTo(MapPrototype input, int x, int y) {
        return wrapped.applicableTo(input, x, y)
            .map(tileConversionAction -> (() -> wrapInDebugTile(tileConversionAction.create())));
    }

    private MapTile wrapInDebugTile(MapTile tile) {
        return new DebugTileWrapper(tile, gameConfig.getPixelBlockSize());
    }
}
