package manfred.game.characters;

import manfred.game.config.GameConfig;
import manfred.game.graphics.paintable.Paintable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Manfred extends MovingObject implements Paintable {
    public static final int ANIMATION_IMAGES_NUMBER = 8;
    private static final int INTERACT_DISTANCE = 10;
    private static final int NEXT_ANIMATION_IMAGE_TRIGGER = 4;

    private int healthPoints;
    private final GameConfig gameConfig;
    private final HashMap<Direction, BufferedImage[]> walkAnimation;

    private int framesCounter = 0;
    private int animationPosition = 0;

    public Manfred(
        int speed,
        int x,
        int y,
        int spriteWidth,
        int spriteHeight,
        int healthPoints,
        MapCollider collider,
        GameConfig gameConfig,
        HashMap<Direction, BufferedImage[]> walkAnimation
    ) {
        super(speed, x, y, spriteWidth, spriteHeight, gameConfig.getPixelBlockSize(), null, collider);
        this.healthPoints = healthPoints;
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

    public void move() {
        super.move();

        if (currentSpeedX == 0 && currentSpeedY == 0) {
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
