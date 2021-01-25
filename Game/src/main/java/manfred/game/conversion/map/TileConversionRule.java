package manfred.game.conversion.map;

import manfred.data.infrastructure.map.ValidatedMapDto;
import manfred.game.config.GameConfig;
import manfred.game.interact.person.gelaber.GelaberConverter;

import java.util.Optional;

public interface TileConversionRule {

    Optional<TileConversionAction> applicableTo(ValidatedMapDto input, int x, int y);;

    default TileConversionRule orElse(TileConversionRule next) {
        return new OrRule(this, next);
    }

    static PersonTileFactory createPerson(GameConfig gameConfig, GelaberConverter gelaberConverter) {
        return new PersonTileFactory(gameConfig, gelaberConverter);
    }

    static TileConversionRule createAccessible() {
        return new AccessibleTileFactory();
    }

    static TileConversionRule createNonAccessible() {
        return new NonAccessibleTileFactory();
    }

    static DecorateTileWithImageRule.Builder decorateWithImage(GameConfig gameConfig) {
        return DecorateTileWithImageRule.build(gameConfig);
    }

    static TileConversionRule createPortal() {
        return new PortalTileFactory();
    }

    static TileConversionRule createDoor() {
        return new DoorTileFactory();
    }
}
