package manfred.game.enemy;

import manfred.game.characters.MapCollider;
import manfred.game.characters.MovingObject;
import manfred.game.graphics.GamePanel;
import manfred.game.graphics.Paintable;

import java.awt.*;

public class Enemy extends MovingObject implements Paintable {
    protected Enemy(int speed, int x, int y, int healthPoints, MapCollider collider) {
        super(speed, x, y, GamePanel.PIXEL_BLOCK_SIZE, GamePanel.PIXEL_BLOCK_SIZE, healthPoints, collider);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(x, y, sizeX, sizeY);
    }
}

