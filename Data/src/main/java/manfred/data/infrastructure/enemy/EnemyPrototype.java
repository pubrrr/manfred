package manfred.data.infrastructure.enemy;

import lombok.Value;
import manfred.data.infrastructure.map.MapObject;
import manfred.data.shared.PositiveInt;

import java.awt.image.BufferedImage;

@Value
public class EnemyPrototype implements MapObject {
    String name;
    PositiveInt healthPoints;
    PositiveInt speed;
    BufferedImage image;
    PositiveInt spawnX;
    PositiveInt spawnY;

    @Override
    public PositiveInt getPositionX() {
        return this.spawnX;
    }

    @Override
    public PositiveInt getPositionY() {
        return this.spawnY;
    }
}
