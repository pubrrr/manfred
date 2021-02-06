package manfred.game.attack;

import manfred.data.shared.PositiveInt;
import manfred.game.characters.Direction;
import manfred.game.characters.Velocity;
import manfred.game.map.Map;

import java.awt.image.BufferedImage;
import java.util.List;

public class AttackGenerator {
    private final PositiveInt speed;
    private final PositiveInt sizeX;
    private final PositiveInt sizeY;
    private final PositiveInt damage;
    private final PositiveInt range;
    private final List<BufferedImage> attackAnimation;
    private final PositiveInt numberOfAnimationImages;

    public AttackGenerator(PositiveInt speed, PositiveInt sizeX, PositiveInt sizeY, PositiveInt damage, PositiveInt range, List<BufferedImage> attackAnimation, PositiveInt numberOfAnimationImages) {
        this.speed = speed;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.damage = damage;
        this.range = range;
        this.attackAnimation = attackAnimation;
        this.numberOfAnimationImages = numberOfAnimationImages;
    }

    public Attack generate(Map.Coordinate castCoordinate, Direction castDirection) {
        return new Attack(
            Velocity.withSpeed(this.speed).accelerate(castDirection),
            castCoordinate,
            this.sizeX,
            this.sizeY,
            this.damage,
            this.range,
            this.attackAnimation,
            this.numberOfAnimationImages
        );
    }
}
