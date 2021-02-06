package manfred.game.attack;

import manfred.data.shared.PositiveInt;
import manfred.game.characters.MovingObject;
import manfred.game.characters.Velocity;
import manfred.game.enemy.Enemy;
import manfred.game.graphics.paintable.LocatedPaintable;
import manfred.game.map.CollisionDetector;
import manfred.game.map.Map;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Attack extends MovingObject implements LocatedPaintable {
    private final PositiveInt damage;
    private final int rangeSquared;
    private boolean resolved = false;
    private final List<BufferedImage> attackAnimation;
    private final PositiveInt numberOfAnimationImages;
    private final long nextAnimationImageTrigger;

    private final Map.Coordinate castPosition;
    private int animationIdx = 0;
    private int framesCounter = 0;

    public Attack(
        Velocity velocity,
        Map.Coordinate initialBottomLeft,
        PositiveInt width,
        PositiveInt height,
        PositiveInt damage,
        PositiveInt range,
        List<BufferedImage> attackAnimation,
        PositiveInt numberOfAnimationImages
    ) {
        super(velocity, initialBottomLeft, width, height, height);
        this.castPosition = this.baseObject.getCenter();
        this.damage = damage;
        this.rangeSquared = range.value() * range.value();
        this.attackAnimation = attackAnimation;
        this.numberOfAnimationImages = numberOfAnimationImages;
        this.nextAnimationImageTrigger = Math.round((double) range.value() / velocity.getMaxSpeed().value() / numberOfAnimationImages.value());
    }

    @Override
    public void checkCollisionsAndMove(CollisionDetector collisionDetector) {
        if (!collisionDetector.isAreaAccessible(this.baseObject)) {
            this.resolve();
            return;
        }
        this.baseObject = this.baseObject.translate(velocity.getVector());

        if (castPosition.distanceTo(this.baseObject.getCenter()).lengthSquared().value() >= rangeSquared) {
            this.resolve();
            return;
        }

        framesCounter++;
        if (framesCounter >= nextAnimationImageTrigger) {
            framesCounter = 0;
            if (animationIdx + 1 < numberOfAnimationImages.value()) {
                animationIdx++;
            }
        }
    }

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
        g.drawImage(
            attackAnimation.get(animationIdx),
            x - offset.x,
            y - offset.y,
            sprite.getWidth(),
            sprite.getSpriteHeight(),
            null
        );
    }

    public void checkHit(Enemy enemy) {
        if (this.collidesWith(enemy)) {
            enemy.takeDamage(this.damage);
            this.resolve();
        }
    }

    private void resolve() {
        this.resolved = true;
    }

    public boolean isResolved() {
        return this.resolved;
    }
}
