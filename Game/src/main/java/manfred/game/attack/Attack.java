package manfred.game.attack;

import manfred.data.shared.PositiveInt;
import manfred.game.characters.MapCollider;
import manfred.game.characters.MovingObject;
import manfred.game.characters.Velocity;
import manfred.game.enemy.Enemy;
import manfred.game.graphics.paintable.LocatedPaintable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Attack extends MovingObject implements LocatedPaintable {
    private final PositiveInt damage;
    private final PositiveInt range;
    private boolean resolved = false;
    private final List<BufferedImage> attackAnimation;
    private final PositiveInt numberOfAnimationImages;
    private final long nextAnimationImageTrigger;

    private final Point castPosition;
    private int animationIdx = 0;
    private int framesCounter = 0;

    public Attack(
        Velocity velocity,
        int x,
        int y,
        PositiveInt width,
        PositiveInt height,
        PositiveInt damage,
        PositiveInt range,
        List<BufferedImage> attackAnimation,
        PositiveInt numberOfAnimationImages
    ) {
        super(velocity, x, y, width, height, height);
        this.castPosition = this.sprite.getCenter();
        this.damage = damage;
        this.range = range;
        this.attackAnimation = attackAnimation;
        this.numberOfAnimationImages = numberOfAnimationImages;
        this.nextAnimationImageTrigger = Math.round((double) range.value() / velocity.getMaxSpeed().value() / numberOfAnimationImages.value());
    }

    @Override
    public void checkCollisionsAndMove(MapCollider mapCollider) {
        if (collidesVertically(mapCollider) || collidesHorizontally(mapCollider)) {
            this.resolve();
        }
        this.sprite.translate(velocity.getVector().x(), velocity.getVector().y());

        if (castPosition.distance(this.sprite.getCenter()) >= range.value()) {
            this.resolve();
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
            sprite.x - offset.x,
            sprite.y - offset.y,
            sprite.width,
            sprite.height,
            null
        );
    }

    public void checkHit(Enemy enemy) {
        if (enemy.getSprite().intersects(this.sprite)) {
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
