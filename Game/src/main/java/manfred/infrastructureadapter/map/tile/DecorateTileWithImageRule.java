package manfred.infrastructureadapter.map.tile;

import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.ValidatedMapTileDto;
import manfred.data.shared.PositiveInt;
import manfred.game.characters.sprite.SimpleSprite;
import manfred.game.config.GameConfig;
import manfred.game.map.MapTile;
import manfred.game.map.MapTileWithSprite;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.function.Function;

public class DecorateTileWithImageRule implements TileConversionRule {

    private final TileConversionRule wrappedRule;
    private final GameConfig gameConfig;

    private DecorateTileWithImageRule(TileConversionRule wrappedRule, GameConfig gameConfig) {
        this.wrappedRule = wrappedRule;
        this.gameConfig = gameConfig;
    }

    @Override
    public Optional<TileConversionAction> applicableTo(MapPrototype input, int x, int y) {
        Optional<ValidatedMapTileDto> tileObject = input.getMap().get(x, y).getTileObject();
        if (tileObject.isEmpty()) {
            return Optional.empty();
        }

        return wrappedRule.applicableTo(input, x, y)
            .map(wrappedTileFactory -> tileWithImageAction(tileObject.get(), wrappedTileFactory));
    }

    private TileFromDtoAction<ValidatedMapTileDto> tileWithImageAction(ValidatedMapTileDto tileObject, TileConversionAction wrappedTileFactory) {
        return new TileFromDtoAction<>(tileObject, tileWithImageFactory(wrappedTileFactory));
    }

    private Function<ValidatedMapTileDto, MapTile> tileWithImageFactory(TileConversionAction tileConversionAction) {
        return validatedMapTileDto -> {
            BufferedImage image = validatedMapTileDto.getImage();
            MapMatrix<String> tileStructure = validatedMapTileDto.getStructure();

            PositiveInt.Strict imageWidth = this.gameConfig.getPixelBlockSize().times(tileStructure.sizeX());
            PositiveInt imageHeight = imageWidth.times(image.getHeight()).divideBy(PositiveInt.ofNonZero(image.getWidth()));

            MapTile wrappedTile = tileConversionAction.create();

            return new MapTileWithSprite(wrappedTile, new SimpleSprite(imageWidth, imageHeight, image));
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

        public DecorateTileWithImageRule and(TileConversionRule wrapped) {
            return new DecorateTileWithImageRule(wrapped, gameConfig);
        }
    }
}
