package manfred.game.characters;

import manfred.data.InvalidInputException;
import manfred.data.shared.PositiveInt;
import manfred.game.config.GameConfig;
import manfred.game.graphics.paintable.LocatedPaintable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Manfred extends MovingObject implements LocatedPaintable {
    public static final int ANIMATION_IMAGES_NUMBER = 8;
    private static final int INTERACT_DISTANCE = 10;
    private static final int NEXT_ANIMATION_IMAGE_TRIGGER = 4;

    private int healthPoints;
    private final GameConfig gameConfig;
    private final HashMap<Direction, BufferedImage[]> walkAnimation;

    private int framesCounter = 0;
    private int animationPosition = 0;

    public Manfred(
        Velocity velocity,
        int x,
        int y,
        PositiveInt spriteWidth,
        PositiveInt spriteHeight,
        PositiveInt healthPoints,
        GameConfig gameConfig,
        HashMap<Direction, BufferedImage[]> walkAnimation
    ) throws InvalidInputException {
        super(velocity, x, y, spriteWidth, spriteHeight, PositiveInt.of(gameConfig.getPixelBlockSize()));
        this.healthPoints = healthPoints.value();
        this.gameConfig = gameConfig;
        this.walkAnimation = walkAnimation;
    }

    public void setX(int x) {
        this.sprite.x = x;
    }

    public void setY(int y) {
        // TODO this needs to be reworked: one has to differentiate between the base y and the sprite y
        // base y is the coordinate that should be used for collisions, so to say the projection to the floor
        // the sprite y is the position of the top edge of the sprite
        this.sprite.y = y + sprite.getBaseHeight() - sprite.getSpriteHeight();
    }

    @Override
    public void checkCollisionsAndMove(MapCollider mapCollider) {
        super.checkCollisionsAndMove(mapCollider);

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

    public Point getCenterMapTile() {
        Point center = this.sprite.getCenter();
        return new Point(center.x / gameConfig.getPixelBlockSize(), center.y / gameConfig.getPixelBlockSize());
    }

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
        System.out.println(viewDirection);
        g.drawImage(
            walkAnimation.get(viewDirection)[animationPosition],
            sprite.x - offset.x,
            sprite.y - offset.y,
            sprite.width,
            sprite.height,
            null
        );
    }

    public Point getInteractionMapTile() {
        Point interactionPoint = viewDirection.interactAtDistance(this.sprite, INTERACT_DISTANCE);
        return new Point(
            interactionPoint.x / gameConfig.getPixelBlockSize(),
            interactionPoint.y / gameConfig.getPixelBlockSize()
        );
    }
}
