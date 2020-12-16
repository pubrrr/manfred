package manfred.game.attack;

import manfred.game.characters.MapCollider;
import manfred.game.characters.MovingObject;
import manfred.game.enemy.Enemy;
import manfred.game.graphics.Paintable;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Attack extends MovingObject implements Paintable {
    private final int damage;
    private final int range;
    private boolean resolved = false;
    private final BufferedImage[] attackAnimation;
    private final int numberOfAnimationImages;
    private final long nextAnimationImageTrigger;

    private final Point castPosition;
    private int animationIdx = 0;
    private int framesCounter = 0;

    public Attack(
        int speed,
        int x,
        int y,
        int width,
        int height,
        MapCollider collider,
        int damage,
        int range,
        BufferedImage[] attackAnimation,
        int numberOfAnimationImages
    ) {
        super(speed, x, y, width, height, height, null, collider);
        this.castPosition = this.sprite.getCenter();
        this.damage = damage;
        this.range = range;
        this.attackAnimation = attackAnimation;
        this.numberOfAnimationImages = numberOfAnimationImages;
        this.nextAnimationImageTrigger = Math.round((double) range / speed / numberOfAnimationImages);
    }

    @Override
    public void move() {
        if (collidesVertically() || collidesHorizontally()) {
            resolve();
        }
        this.sprite.translate(currentSpeedX, currentSpeedY);

        if (castPosition.distance(this.sprite.getCenter()) >= range) {
            this.resolve();
        }

        framesCounter++;
        if (framesCounter >= nextAnimationImageTrigger) {
            framesCounter = 0;
            if (animationIdx + 1 < numberOfAnimationImages) {
                animationIdx++;
            }
        }
    }

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
        g.drawImage(
            attackAnimation[animationIdx],
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
