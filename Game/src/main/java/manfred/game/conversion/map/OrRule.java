package manfred.game.conversion.map;

import manfred.data.map.ValidatedMapDto;

import java.util.Optional;

public class OrRule implements TileConversionRule {
    private final TileConversionRule wrapped;
    private final TileConversionRule or;

    public OrRule(TileConversionRule wrapped, TileConversionRule or) {
        this.wrapped = wrapped;
        this.or = or;
    }

    @Override
    public Optional<TileConversionAction> applicableTo(ValidatedMapDto input, int x, int y) {
        return wrapped.applicableTo(input, x, y)
            .or(() -> or.applicableTo(input, x, y));
    }
}