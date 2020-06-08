package manfred.game.attack;

import manfred.game.characters.MapCollider;
import manfred.game.characters.MovingObject;
import manfred.game.graphics.Paintable;

import java.awt.*;

public class Attack extends MovingObject implements Paintable {
    protected Attack(int speed, int x, int y, int sizeX, int sizeY, MapCollider collider) {
        super(speed, x, y, sizeX, sizeY, collider);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(this.x, this.y, this.sizeX, this.sizeY);
    }
}
