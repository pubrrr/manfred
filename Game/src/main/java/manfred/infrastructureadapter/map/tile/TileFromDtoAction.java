package manfred.infrastructureadapter.map.tile;

import lombok.AllArgsConstructor;
import manfred.data.infrastructure.map.TileConversionAction;
import manfred.game.map.MapTile;

import java.util.function.Function;

@AllArgsConstructor
public class TileFromDtoAction<T> implements TileConversionAction<MapTile> {

    private final T dto;
    private final Function<T, MapTile> factory;

    @Override
    public MapTile create() {
        return factory.apply(dto);
    }
}

