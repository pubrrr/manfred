package manfred.infrastructureadapter.map.tile;

import manfred.data.infrastructure.map.MapPrototype;
import manfred.game.config.GameConfig;
import manfred.infrastructureadapter.person.gelaber.GelaberConverter;

import java.util.Optional;

public interface TileConversionRule {

    Optional<TileConversionAction> applicableTo(MapPrototype input, int x, int y);;

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
