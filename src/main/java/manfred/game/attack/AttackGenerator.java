package manfred.game.attack;

import manfred.game.characters.Direction;
import manfred.game.characters.MapCollider;

public class AttackGenerator {
    private final int speed;
    private final int sizeX;
    private final int sizeY;
    private MapCollider mapCollider;

    public AttackGenerator(int speed, int sizeX, int sizeY, MapCollider mapCollider) {
        this.speed = speed;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.mapCollider = mapCollider;
    }

    public Attack generate(int x, int y, Direction castDirection) {
        Attack attack = new Attack(this.speed, x - this.sizeX / 2, y - this.sizeY / 2, this.sizeX, this.sizeY, mapCollider);
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
