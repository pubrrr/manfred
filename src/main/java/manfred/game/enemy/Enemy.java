package manfred.game.enemy;

import manfred.game.characters.Manfred;
import manfred.game.characters.MapCollider;
import manfred.game.characters.MovingObject;
import manfred.game.graphics.GamePanel;
import manfred.game.graphics.Paintable;

import java.awt.*;

public class Enemy extends MovingObject implements Paintable {
    private String name;

    private int healthPoints;
    private int aggroRadius;

    public Enemy(String name, int speed, int x, int y, int healthPoints, MapCollider collider, int aggroRadius) {
        super(speed, x, y, GamePanel.PIXEL_BLOCK_SIZE, GamePanel.PIXEL_BLOCK_SIZE, collider);
        this.name = name;
        this.healthPoints = healthPoints;
        this.aggroRadius = aggroRadius;
    }

    @Override
    public void paint(Graphics g, Point offset) {
        g.setColor(Color.BLACK);
        g.fillPolygon(this.sprite.toPaint(offset));

        g.setFont(new Font("Palatino Linotype", Font.BOLD, this.sprite.height / 2));

        g.setColor(Color.RED);
        g.drawString(String.valueOf(this.healthPoints), this.sprite.x + this.sprite.width / 4 - offset.x, this.sprite.y + (this.sprite.height * 3 / 4) - offset.y);

        g.setColor(Color.BLACK);
        g.drawString(this.name, this.sprite.x + this.sprite.width / 4 - offset.x , this.sprite.y - (this.sprite.height / 4) - offset.y);
    }

    public void move(Manfred manfred) {
        long distanceX = manfred.getX() - this.sprite.x;
        long distanceY = manfred.getY() - this.sprite.y;
        long distanceSquared = distanceX * distanceX + distanceY * distanceY;

        if (distanceSquared <= aggroRadius * aggroRadius) {
            // this actually makes the top left corner of the enemy move towards the top left corner of Manfred. Change when necessary
            double distance = Math.sqrt(distanceSquared);
            currentSpeedX = (int) (speed * ((float) distanceX) / distance);
            currentSpeedY = (int) (speed * ((float) distanceY) / distance);
        } else {
            currentSpeedX = 0;
            currentSpeedY = 0;
        }

        super.move();
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void takeDamage(int damage) {
        this.healthPoints -= damage;
    }
}

