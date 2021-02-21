package manfred.infrastructureadapter.map.tile;

import lombok.AllArgsConstructor;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.TileConversionAction;
import manfred.data.infrastructure.map.TileConversionRule;
import manfred.game.config.GameConfig;
import manfred.game.map.DebugTileWrapper;
import manfred.game.map.MapTile;

import java.util.Optional;

@AllArgsConstructor
public class DebugWrapperFactory implements TileConversionRule<MapTile> {

    private final TileConversionRule<MapTile> wrapped;
    private final GameConfig gameConfig;

    @Override
    public Optional<TileConversionAction<MapTile>> applicableTo(MapPrototype input, MapPrototype.Coordinate coordinate) {
        return wrapped.applicableTo(input, coordinate)
            .map(tileConversionAction -> (() -> wrapInDebugTile(tileConversionAction.create())));
    }

    private MapTile wrapInDebugTile(MapTile tile) {
        return new DebugTileWrapper(tile, gameConfig.getPixelBlockSize());
    }
}
