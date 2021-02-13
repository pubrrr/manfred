package manfred.game.attack;

import manfred.data.shared.PositiveInt;
import manfred.game.characters.Direction;
import manfred.game.characters.Velocity;
import manfred.game.characters.sprite.AnimatedSpriteCloneFactory;
import manfred.game.map.Map;

public class AttackGenerator {
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

    public Attack generate(Map.Coordinate castCoordinate, Direction castDirection) {
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
