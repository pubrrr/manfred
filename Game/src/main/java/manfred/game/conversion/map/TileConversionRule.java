package manfred.game.conversion.map;

import manfred.data.map.ValidatedMapDto;

import java.util.Optional;

public interface TileConversionRule {

    Optional<TileConversionAction> applicableTo(ValidatedMapDto input, int x, int y);;

    default TileConversionRule orElse(TileConversionRule next) {
        return new OrRule(this, next);
    }

    static PersonTileFactory createPerson() {
        return new PersonTileFactory();
    }

    static TileConversionRule createAccessible() {
        return new AccessibleTileFactory();
    }

    static TileConversionRule createNonAccessible() {
        return new NonAccessibleTileFactory();
    }
}
