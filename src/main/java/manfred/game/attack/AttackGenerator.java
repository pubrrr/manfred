package manfred.game.attack;

import manfred.game.characters.Direction;
import manfred.game.characters.MapCollider;

import java.awt.*;

public class AttackGenerator {
    private final int speed;
    private final int sizeX;
    private final int sizeY;
    private MapCollider mapCollider;
    private int damage;
    private int range;

    public AttackGenerator(int speed, int sizeX, int sizeY, MapCollider mapCollider, int damage, int range) {
        this.speed = speed;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.mapCollider = mapCollider;
        this.damage = damage;
        this.range = range;
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
                this.range
        );
        switch (castDirection) {
            case up:
                attack.up();
                break;
            case down:
                attack.down();
                break;
            case left:
                attack.left();
                break;
            case right:
                attack.right();
                break;
        }
        return attack;
    }
}
