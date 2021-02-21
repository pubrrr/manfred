package manfred.infrastructureadapter.map.tile;

import manfred.data.infrastructure.map.TileConversionRule;
import manfred.game.config.GameConfig;
import manfred.game.map.MapTile;
import manfred.infrastructureadapter.person.gelaber.GelaberConverter;

public interface GameTileFactoryRules {

    static PersonTileFactory createPerson(GameConfig gameConfig, GelaberConverter gelaberConverter) {
        return new PersonTileFactory(gameConfig, gelaberConverter);
    }

    static TileConversionRule<MapTile> createAccessible() {
        return new AccessibleTileFactory();
    }

    static TileConversionRule<MapTile> createNonAccessible() {
        return new NonAccessibleTileFactory();
    }

    static DecorateTileWithImageRule.Builder decorateWithSprite(GameConfig gameConfig) {
        return DecorateTileWithImageRule.builder(gameConfig);
    }

    static TileConversionRule<MapTile> createPortal() {
        return new PortalTileFactory();
    }

    static TileConversionRule<MapTile> createDoor() {
        return new DoorTileFactory();
    }

    static TileConversionRule<MapTile> wrapForGraphicsDebugging(TileConversionRule<MapTile> wrapped, GameConfig gameConfig) {
        return new DebugWrapperFactory(wrapped, gameConfig);
    }
}
