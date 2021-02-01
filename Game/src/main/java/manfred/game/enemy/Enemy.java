package manfred.game.enemy;

import manfred.data.InvalidInputException;
import manfred.data.shared.PositiveInt;
import manfred.game.config.GameConfig;
import manfred.game.characters.Manfred;
import manfred.game.characters.MovingObject;
import manfred.game.graphics.paintable.LocatedPaintable;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends MovingObject implements LocatedPaintable {
    private final String name;
    private final GameConfig gameConfig;
    private final BufferedImage image;
    private final int aggroRadius;

    private int healthPoints;

    public Enemy(String name, PositiveInt speed, int x, int y, PositiveInt healthPoints, BufferedImage image, int aggroRadius, GameConfig gameConfig) throws InvalidInputException {
        super(speed, x, y, PositiveInt.of(2 * gameConfig.getPixelBlockSize()), PositiveInt.of(2 * gameConfig.getPixelBlockSize()), PositiveInt.of(2 * gameConfig.getPixelBlockSize()));
        this.name = name;
        this.healthPoints = healthPoints.value(); // TODO
        this.image = image;
        this.aggroRadius = aggroRadius;
        this.gameConfig = gameConfig;
    }

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
        g.drawImage(this.image, this.sprite.x - offset.x, this.sprite.y - offset.y, this.sprite.width, this.sprite.height, null);

        g.setFont(new Font("Palatino Linotype", Font.BOLD, gameConfig.getPixelBlockSize() / 2));

        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(this.healthPoints), this.sprite.x + this.sprite.width / 4 - offset.x, this.sprite.getBottom() + (this.sprite.height / 2) - offset.y);

        g.setColor(Color.BLACK);
        g.drawString(this.name, this.sprite.x + this.sprite.width / 4 - offset.x, this.sprite.y - (this.sprite.height / 4) - offset.y);
    }

    public Enemy determineSpeed(Manfred manfred) {
        long distanceX = manfred.getX() - this.sprite.x;
        long distanceY = manfred.getY() - this.sprite.y;
        long distanceSquared = distanceX * distanceX + distanceY * distanceY;

        if (distanceSquared <= (long) aggroRadius * aggroRadius) {
            // this actually makes the top left corner of the enemy move towards the top left corner of Manfred. Change when necessary
            double distance = Math.sqrt(distanceSquared);
            currentSpeedX = (int) (speed * ((float) distanceX) / distance);
            currentSpeedY = (int) (speed * ((float) distanceY) / distance);
        } else {
            currentSpeedX = 0;
            currentSpeedY = 0;
        }
        return this;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void takeDamage(PositiveInt damage) {
        this.healthPoints -= damage.value();
    }
}

