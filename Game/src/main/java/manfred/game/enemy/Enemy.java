package manfred.game.enemy;

import manfred.data.shared.PositiveInt;
import manfred.game.characters.Manfred;
import manfred.game.characters.MovingObject;
import manfred.game.characters.Velocity;
import manfred.game.config.GameConfig;
import manfred.game.graphics.paintable.LocatedPaintable;
import manfred.game.map.Map;
import manfred.game.geometry.Vector;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends MovingObject implements LocatedPaintable {
    private final String name;
    private final GameConfig gameConfig;
    private final BufferedImage image;
    private final int aggroRadiusSquared;

    private int healthPoints;

    public Enemy(String name, Velocity velocity, Map.Coordinate initialBottomLeft, PositiveInt healthPoints, BufferedImage image, int aggroRadius, GameConfig gameConfig) {
        super(velocity, initialBottomLeft, PositiveInt.of(gameConfig.getPixelBlockSize().times(2)), PositiveInt.of(gameConfig.getPixelBlockSize().times(2)), PositiveInt.of(gameConfig.getPixelBlockSize().times(2)));
        this.name = name;
        this.healthPoints = healthPoints.value(); // TODO
        this.image = image;
        this.aggroRadiusSquared = aggroRadius * aggroRadius;
        this.gameConfig = gameConfig;
    }

    @Override
    public void paint(Graphics g, Integer x, Integer y) {
        g.drawImage(this.image, x, y, this.sprite.getWidth(), this.sprite.getSpriteHeight(), null);

        g.setFont(new Font("Palatino Linotype", Font.BOLD, gameConfig.getPixelBlockSize().divideBy(2)));

        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(this.healthPoints), x + this.sprite.getWidth() / 4, y + (this.sprite.getSpriteHeight() / 2));

        g.setColor(Color.BLACK);
        g.drawString(this.name, x + this.sprite.getWidth() / 4, y - (this.sprite.getSpriteHeight() / 4));
    }

    public Enemy determineSpeed(Manfred manfred) {
        Vector enemyToManfred = this.baseObject.getCenter().distanceTo(manfred.getCenter());
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

