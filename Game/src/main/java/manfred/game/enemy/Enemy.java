package manfred.game.enemy;

import manfred.data.shared.PositiveInt;
import manfred.game.characters.Manfred;
import manfred.game.characters.MovingObject;
import manfred.game.characters.Velocity;
import manfred.game.characters.sprite.SimpleSprite;
import manfred.game.config.GameConfig;
import manfred.game.geometry.Vector;
import manfred.game.graphics.GraphicsAdapter;
import manfred.game.graphics.PanelCoordinate;
import manfred.game.graphics.paintable.LocatedPaintable;
import manfred.game.map.Map;

import java.awt.*;

public class Enemy extends MovingObject<SimpleSprite> implements LocatedPaintable {
    private final String name;
    private final GameConfig gameConfig;
    private final int aggroRadiusSquared;

    private int healthPoints;

    public Enemy(String name, Velocity velocity, Map.Coordinate initialBottomLeft, PositiveInt healthPoints, PositiveInt.Strict aggroRadius, GameConfig gameConfig, SimpleSprite sprite) {
        super(velocity, initialBottomLeft, gameConfig.getPixelBlockSize().times(2), gameConfig.getPixelBlockSize().times(2), sprite);
        this.name = name;
        this.healthPoints = healthPoints.value();
        this.aggroRadiusSquared = aggroRadius.value() * aggroRadius.value();
        this.gameConfig = gameConfig;
    }

    @Override
    public void paint(GraphicsAdapter g, PanelCoordinate bottomLeftCoordinate) {
        super.paint(g, bottomLeftCoordinate);

        g.setFont(new Font("Palatino Linotype", Font.BOLD, gameConfig.getPixelBlockSize().divideBy(2)));

        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(this.healthPoints), bottomLeftCoordinate.getX() + this.sprite.getWidth().value() / 4, bottomLeftCoordinate.getY() + (this.sprite.getHeight().value() / 2));

        g.setColor(Color.BLACK);
        g.drawString(this.name, bottomLeftCoordinate.getX() + this.sprite.getWidth().value() / 4, bottomLeftCoordinate.getY() - (this.sprite.getHeight().value() / 4));
    }

    public Enemy determineSpeed(Manfred manfred) {
        Vector<Map.Coordinate> enemyToManfred = this.baseObject.getCenter().distanceTo(manfred.getCenter());
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

