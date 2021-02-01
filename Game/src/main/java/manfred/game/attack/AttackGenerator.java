package manfred.game.attack;

import manfred.data.shared.PositiveInt;
import manfred.game.characters.Direction;

import java.awt.*;
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

    public Attack generate(Point center, Direction castDirection) {
        Attack attack = new Attack(
                this.speed,
                center.x - this.sizeX.value() / 2,
                center.y - this.sizeY.value() / 2,
                this.sizeX,
                this.sizeY,
                this.damage,
                this.range,
                this.attackAnimation,
                this.numberOfAnimationImages
        );
        castDirection.kickAttack(attack);
        return attack;
    }
}
