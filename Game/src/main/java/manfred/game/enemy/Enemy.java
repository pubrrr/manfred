package manfred.game.enemy;

import manfred.game.config.GameConfig;
import manfred.game.characters.Manfred;
import manfred.game.characters.MapCollider;
import manfred.game.characters.MovingObject;
import manfred.game.graphics.paintable.Paintable;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends MovingObject implements Paintable {
    private final String name;
    private final GameConfig gameConfig;
    private final int aggroRadius;

    private int healthPoints;

    public Enemy(String name, int speed, int x, int y, int healthPoints, BufferedImage image, MapCollider collider, int aggroRadius, GameConfig gameConfig) {
        super(speed, x, y, 2 * gameConfig.getPixelBlockSize(), 2 * gameConfig.getPixelBlockSize(), 2 * gameConfig.getPixelBlockSize(), image, collider);
        this.name = name;
        this.healthPoints = healthPoints;
        this.aggroRadius = aggroRadius;
        this.gameConfig = gameConfig;
    }

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
        sprite.paint(g, offset, x, y);

        g.setFont(new Font("Palatino Linotype", Font.BOLD, gameConfig.getPixelBlockSize() / 2));

        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(this.healthPoints), this.sprite.x + this.sprite.width / 4 - offset.x, this.sprite.getBottom() + (this.sprite.height / 2) - offset.y);

        g.setColor(Color.BLACK);
        g.drawString(this.name, this.sprite.x + this.sprite.width / 4 - offset.x, this.sprite.y - (this.sprite.height / 4) - offset.y);
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

