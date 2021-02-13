package manfred.game.attack;

import manfred.data.shared.PositiveInt;
import manfred.game.characters.MovingObject;
import manfred.game.characters.Velocity;
import manfred.game.characters.sprite.AnimatedSprite;
import manfred.game.enemy.Enemy;
import manfred.game.geometry.Vector;
import manfred.game.graphics.paintable.LocatedPaintable;
import manfred.game.map.CollisionDetector;
import manfred.game.map.Map;

public class Attack extends MovingObject<AnimatedSprite> implements LocatedPaintable {
    private final PositiveInt damage;
    private final int rangeSquared;
    private boolean resolved = false;
    private final Map.Coordinate castPosition;

    public Attack(
        Velocity velocity,
        Map.Coordinate initialBottomLeft,
        PositiveInt width,
        PositiveInt height,
        PositiveInt damage,
        PositiveInt range,
        AnimatedSprite sprite
    ) {
        super(velocity, initialBottomLeft, width, height, sprite);
        this.castPosition = this.baseObject.getCenter();
        this.damage = damage;
        this.rangeSquared = range.value() * range.value();
    }

    @Override
    public void checkCollisionsAndMove(CollisionDetector collisionDetector) {
        if (!collisionDetector.isAreaAccessible(this.baseObject)) {
            this.resolve();
            return;
        }
        this.baseObject = this.baseObject.translate(velocity.getVector());

        Vector<Map.Coordinate> travelledDistance = castPosition.distanceTo(this.baseObject.getCenter());
        if (travelledDistance.lengthSquared().value() >= rangeSquared) {
            this.resolve();
            return;
        }

        sprite.tick();
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
