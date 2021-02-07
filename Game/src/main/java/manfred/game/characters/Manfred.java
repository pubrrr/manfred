package manfred.game.characters;

import manfred.data.shared.PositiveInt;
import manfred.game.config.GameConfig;
import manfred.game.geometry.Vector;
import manfred.game.graphics.PanelCoordinate;
import manfred.game.graphics.paintable.LocatedPaintable;
import manfred.game.map.CollisionDetector;
import manfred.game.map.Map;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Manfred extends MovingObject implements LocatedPaintable {
    public static final int ANIMATION_IMAGES_NUMBER = 8;
    private static final PositiveInt.Strict INTERACT_DISTANCE = PositiveInt.ofNonZero(40);
    private static final int NEXT_ANIMATION_IMAGE_TRIGGER = 4;

    private int healthPoints;
    private final GameConfig gameConfig;
    private final HashMap<Direction, BufferedImage[]> walkAnimation;

    private int framesCounter = 0;
    private int animationPosition = 0;

    public Manfred(
        Velocity velocity,
        Map.Coordinate initialBottomLeft,
        PositiveInt spriteWidth,
        PositiveInt spriteHeight,
        PositiveInt healthPoints,
        GameConfig gameConfig,
        HashMap<Direction, BufferedImage[]> walkAnimation
    ) {
        super(velocity, initialBottomLeft, PositiveInt.of(spriteWidth.value() - 2), spriteHeight, PositiveInt.of(gameConfig.getPixelBlockSize().value() - 2));
        this.healthPoints = healthPoints.value();
        this.gameConfig = gameConfig;
        this.walkAnimation = walkAnimation;
    }

    public void setToTile(Map.TileCoordinate tileCoordinate) {
        this.baseObject = baseObject.moveTo(tileCoordinate.getBottomLeftCoordinate());
    }

    @Override
    public void checkCollisionsAndMove(CollisionDetector collisionDetector) {
        super.checkCollisionsAndMove(collisionDetector);

        if (this.velocity.getVector().lengthSquared().value() == 0) {
            framesCounter = 0;
            animationPosition = 0;
        } else {
            framesCounter++;
            if (framesCounter >= NEXT_ANIMATION_IMAGE_TRIGGER) {
                framesCounter = 0;
                animationPosition++;
                if (animationPosition >= ANIMATION_IMAGES_NUMBER) {
                    animationPosition = 0;
                }
            }
        }
    }

    public Map.Coordinate getCenter() {
        return this.baseObject.getCenter();
    }

    @Override
    public void paint(Graphics g, PanelCoordinate coordinate) {
        System.out.println("buh");
        g.drawImage(
            walkAnimation.get(viewDirection)[animationPosition],
            coordinate.getX(),
            coordinate.getY() - gameConfig.getPixelBlockSize().value(), // TODO!
            sprite.getWidth(),
            sprite.getSpriteHeight(),
            null
        );
    }

    public Map.TileCoordinate getInteractionMapTile() {
        Vector.NonZero<Map.Coordinate> toInteractionPoint = this.viewDirection.getVector().scalteToLength(INTERACT_DISTANCE);
        Map.Coordinate interactionPoint = this.baseObject.getCenter().translate(toInteractionPoint);

        return interactionPoint.getTile();
    }
}
