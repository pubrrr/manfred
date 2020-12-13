package manfred.game.attack;

import manfred.game.characters.Direction;
import manfred.game.characters.MapCollider;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AttackGenerator {
    private final int speed;
    private final int sizeX;
    private final int sizeY;
    private MapCollider mapCollider;
    private int damage;
    private int range;
    private BufferedImage[] attackAnimation;
    private int numberOfAnimationImages;

    public AttackGenerator(int speed, int sizeX, int sizeY, MapCollider mapCollider, int damage, int range, BufferedImage[] attackAnimation, int numberOfAnimationImages) {
        this.speed = speed;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.mapCollider = mapCollider;
        this.damage = damage;
        this.range = range;
        this.attackAnimation = attackAnimation;
        this.numberOfAnimationImages = numberOfAnimationImages;
    }

    public Attack generate(Point center, Direction castDirection) {
        Attack attack = new Attack(
                this.speed,
                center.x - this.sizeX / 2,
                center.y - this.sizeY / 2,
                this.sizeX,
                this.sizeY,
                mapCollider,
                this.damage,
                this.range,
                this.attackAnimation,
                this.numberOfAnimationImages
        );
        switch (castDirection) {
            case UP:
                attack.up();
                break;
            case DOWN:
                attack.down();
                break;
            case LEFT:
                attack.left();
                break;
            case RIGHT:
                attack.right();
                break;
        }
        return attack;
    }
}
