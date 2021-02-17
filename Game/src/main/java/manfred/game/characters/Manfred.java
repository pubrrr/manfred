package manfred.game.characters;

import manfred.data.shared.PositiveInt;
import manfred.game.characters.sprite.DirectionalAnimatedSprite;
import manfred.game.geometry.Vector;
import manfred.game.map.CollisionDetector;
import manfred.game.map.Map;

public class Manfred extends MovingObject<DirectionalAnimatedSprite> {
    public static final int ANIMATION_IMAGES_NUMBER = 8;
    private static final PositiveInt.Strict INTERACT_DISTANCE = PositiveInt.ofNonZero(40);

    private int healthPoints;

    public Manfred(
        Velocity velocity,
        Map.Coordinate initialBottomLeft,
        PositiveInt healthPoints,
        PositiveInt width,
        PositiveInt depth,
        DirectionalAnimatedSprite sprite
    ) {
        super(velocity, initialBottomLeft, width, depth, sprite);
        this.healthPoints = healthPoints.value();
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

    public Map.TileCoordinate getInteractionMapTile() {
        Vector.NonZero<Map.Coordinate> toInteractionPoint = this.viewDirection.getUnitVector().scaleToLength(INTERACT_DISTANCE);
        Map.Coordinate interactionPoint = this.baseObject.getCenter().translate(toInteractionPoint);

        return interactionPoint.getTile();
    }
}
