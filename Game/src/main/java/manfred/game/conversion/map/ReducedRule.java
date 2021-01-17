package manfred.game.conversion.map;

import lombok.AllArgsConstructor;
import manfred.game.map.MapTile;

import java.util.function.Function;

@AllArgsConstructor
public class ReducedRule<T> implements TileConversionAction {

    private final T dto;
    private final Function<T, MapTile> factory;

    @Override
    public MapTile create() {
        return factory.apply(dto);
    }
}

