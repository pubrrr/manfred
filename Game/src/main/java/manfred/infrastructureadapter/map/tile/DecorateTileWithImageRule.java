package manfred.infrastructureadapter.map.tile;

import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.TileConversionAction;
import manfred.data.infrastructure.map.TileConversionRule;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.data.infrastructure.map.tile.ValidatedMapTileDto;
import manfred.data.shared.PositiveInt;
import manfred.game.characters.sprite.SimpleSprite;
import manfred.game.config.GameConfig;
import manfred.game.map.MapTile;
import manfred.game.map.MapTileWithSprite;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.function.Function;

public class DecorateTileWithImageRule implements TileConversionRule<MapTile> {

    private final TileConversionRule<MapTile> wrappedRule;
    private final GameConfig gameConfig;

    private DecorateTileWithImageRule(TileConversionRule<MapTile> wrappedRule, GameConfig gameConfig) {
        this.wrappedRule = wrappedRule;
        this.gameConfig = gameConfig;
    }

    @Override
    public Optional<TileConversionAction<MapTile>> applicableTo(MapPrototype input, MapPrototype.Coordinate coordinate) {
        Optional<ValidatedMapTileDto> tileObject = input.getFromMap(coordinate).getTileObject();
        if (tileObject.isEmpty()) {
            return Optional.empty();
        }

        return wrappedRule.applicableTo(input, coordinate)
            .map(wrappedTileFactory -> tileWithImageAction(tileObject.get(), wrappedTileFactory));
    }

    private TileFromDtoAction<ValidatedMapTileDto> tileWithImageAction(ValidatedMapTileDto tileObject, TileConversionAction<MapTile> wrappedTileFactory) {
        return new TileFromDtoAction<>(tileObject, tileWithImageFactory(wrappedTileFactory));
    }

    private Function<ValidatedMapTileDto, MapTile> tileWithImageFactory(TileConversionAction<MapTile> tileConversionAction) {
        return validatedMapTileDto -> {
            BufferedImage image = validatedMapTileDto.getImage();

            PositiveInt.Strict imageWidth = this.gameConfig.getPixelBlockSize().times(validatedMapTileDto.getStructure().getSizeX());
            PositiveInt imageHeight = imageWidth.times(image.getHeight()).divideBy(PositiveInt.ofNonZero(image.getWidth()));

            MapPrototype.Coordinate originCoordinate = validatedMapTileDto.getOriginCoordinate();
            int yOffset = originCoordinate.getY().times(this.gameConfig.getPixelBlockSize()).value();

            MapTile wrappedTile = tileConversionAction.create();

            return new MapTileWithSprite(wrappedTile, new SimpleSprite(imageWidth, imageHeight, yOffset, image));
        };
    }

    public static Builder builder(GameConfig gameConfig) {
        return new Builder(gameConfig);
    }

    public static class Builder {
        private final GameConfig gameConfig;

        Builder(GameConfig gameConfig) {
            this.gameConfig = gameConfig;
        }

        public DecorateTileWithImageRule and(TileConversionRule<MapTile> wrapped) {
            return new DecorateTileWithImageRule(wrapped, gameConfig);
        }
    }
}
