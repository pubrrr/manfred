package manfred.game.attack;

import manfred.game.characters.MapCollider;
import manfred.game.characters.MovingObject;
import manfred.game.controls.KeyControls;
import manfred.game.enemy.Enemy;
import manfred.game.graphics.Paintable;

import java.awt.*;
import java.util.function.Consumer;

public class Attack extends MovingObject implements Paintable {
    private int damage;
    private int range;
    private boolean resolved = false;

    private final Point castPosition;

    protected Attack(int speed, int x, int y, int sizeX, int sizeY, MapCollider collider, int damage, int range) {
        super(speed, x, y, sizeX, sizeY, collider);
        this.castPosition = this.sprite.getCenter();
        this.damage = damage;
        this.range = range;
    }

    @Override
    public Consumer<KeyControls> move() {
        if (castPosition.distance(this.sprite.getCenter()) > range) {
            this.resolve();
        }
        return super.move();
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.MAGENTA);
        g.fillPolygon(this.sprite.toPaint());
    }

    public void hit(Enemy enemy) {
        if (enemy.intersectsSprite(this.sprite)) {
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
