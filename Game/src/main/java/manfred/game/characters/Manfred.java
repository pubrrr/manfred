package manfred.game.characters;

import manfred.data.shared.PositiveInt;
import manfred.game.characters.sprite.DirectionalAnimatedSprite;
import manfred.game.config.GameConfig;
import manfred.game.geometry.Vector;
import manfred.game.graphics.PanelCoordinate;
import manfred.game.graphics.paintable.LocatedPaintable;
import manfred.game.map.CollisionDetector;
import manfred.game.map.Map;

import java.awt.*;

public class Manfred extends MovingObject<DirectionalAnimatedSprite> implements LocatedPaintable {
    public static final int ANIMATION_IMAGES_NUMBER = 8;
    private static final PositiveInt.Strict INTERACT_DISTANCE = PositiveInt.ofNonZero(40);

    private int healthPoints;
    private final GameConfig gameConfig;

    public Manfred(
        Velocity velocity,
        Map.Coordinate initialBottomLeft,
        PositiveInt healthPoints,
        GameConfig gameConfig,
        DirectionalAnimatedSprite sprite
    ) {
        super(velocity, initialBottomLeft, PositiveInt.of(gameConfig.getPixelBlockSize().value() - 2), PositiveInt.of(gameConfig.getPixelBlockSize().value() - 2), sprite);
        this.healthPoints = healthPoints.value();
        this.gameConfig = gameConfig;
    }

    public void setToTile(Map.TileCoordinate tileCoordinate) {
        this.baseObject = baseObject.moveTo(tileCoordinate.getBottomLeftCoordinate());
    }

    @Override
    public void checkCollisionsAndMove(CollisionDetector collisionDetector) {
        super.checkCollisionsAndMove(collisionDetector);

        if (this.velocity.getVector().lengthSquared().value() == 0) {
            this.sprite.stopAnimation();
        } else {
            this.sprite.tick(this.viewDirection);
        }
    }

    public Map.Coordinate getCenter() {
        return this.baseObject.getCenter();
    }

    @Override
    public void paint(Graphics g, PanelCoordinate coordinate) {
        g.drawImage(
            this.sprite.getImage(),
            coordinate.getX(),
            coordinate.getY() - gameConfig.getPixelBlockSize().value(), // TODO!
            this.sprite.getWidth().value(),
            this.sprite.getHeight().value(),
            null
        );
    }

    public Map.TileCoordinate getInteractionMapTile() {
        Vector.NonZero<Map.Coordinate> toInteractionPoint = this.viewDirection.getVector().scaleToLength(INTERACT_DISTANCE);
        Map.Coordinate interactionPoint = this.baseObject.getCenter().translate(toInteractionPoint);

        return interactionPoint.getTile();
    }
}
