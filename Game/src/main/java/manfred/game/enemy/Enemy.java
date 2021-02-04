package manfred.game.enemy;

import manfred.data.InvalidInputException;
import manfred.data.shared.PositiveInt;
import manfred.game.characters.Velocity;
import manfred.game.config.GameConfig;
import manfred.game.characters.Manfred;
import manfred.game.characters.MovingObject;
import manfred.game.graphics.paintable.LocatedPaintable;
import manfred.game.map.Vector;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends MovingObject implements LocatedPaintable {
    private final String name;
    private final GameConfig gameConfig;
    private final BufferedImage image;
    private final int aggroRadiusSquared;

    private int healthPoints;

    public Enemy(String name, Velocity velocity, int x, int y, PositiveInt healthPoints, BufferedImage image, int aggroRadius, GameConfig gameConfig) throws InvalidInputException {
        super(velocity, x, y, PositiveInt.of(2 * gameConfig.getPixelBlockSize()), PositiveInt.of(2 * gameConfig.getPixelBlockSize()), PositiveInt.of(2 * gameConfig.getPixelBlockSize()));
        this.name = name;
        this.healthPoints = healthPoints.value(); // TODO
        this.image = image;
        this.aggroRadiusSquared = aggroRadius * aggroRadius;
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
        // TODO take Manfred's center rather than some corner
        int distanceX = manfred.getX() - this.sprite.x;
        int distanceY = manfred.getY() - this.sprite.y;

        Vector enemyToManfred = Vector.of(distanceX, distanceY);
        if (enemyToManfred.lengthSquared().value() <= this.aggroRadiusSquared) {
            this.velocity = velocity.moveInDirection(enemyToManfred);
        } else {
            this.velocity = velocity.stop();
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

