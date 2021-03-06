package manfred.game.attack;

import manfred.data.shared.PositiveInt;
import manfred.game.characters.Direction;
import manfred.game.characters.Velocity;
import manfred.game.characters.sprite.AnimatedSpriteCloneFactory;
import manfred.game.geometry.Vector;
import manfred.game.map.Map;

public class AttackGenerator {
    private final static PositiveInt.Strict CAST_DISTANCE = PositiveInt.ofNonZero(20);

    private final PositiveInt speed;
    private final PositiveInt sizeX;
    private final PositiveInt sizeY;
    private final PositiveInt damage;
    private final PositiveInt range;
    private final AnimatedSpriteCloneFactory sprite;

    public AttackGenerator(PositiveInt speed, PositiveInt sizeX, PositiveInt sizeY, PositiveInt damage, PositiveInt range, AnimatedSpriteCloneFactory sprite) {
        this.speed = speed;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.damage = damage;
        this.range = range;
        this.sprite = sprite;
    }

    public Attack generate(Map.Coordinate manfredCenterCoordinate, Direction castDirection) {
        Map.Coordinate castCoordinate = manfredCenterCoordinate.translate(castDirection.getUnitVector().scaleToLength(CAST_DISTANCE))
            .translate(Vector.pointingRight(-this.sizeX.value() / 2))
            .translate(Vector.pointingUp(-this.sizeY.value() / 2));

        return new Attack(
            Velocity.withSpeed(this.speed).accelerate(castDirection),
            castCoordinate,
            this.sizeX,
            this.sizeY,
            this.damage,
            this.range,
            this.sprite.buildClone()
        );
    }
}
