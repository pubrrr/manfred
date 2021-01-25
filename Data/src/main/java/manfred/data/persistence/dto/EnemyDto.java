package manfred.data.persistence.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import manfred.data.infrastructure.enemy.EnemyPrototype;

import java.awt.image.BufferedImage;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnemyDto {
    private String name;
    private int healthPoints;
    private int speed;

    @JsonIgnore
    private BufferedImage image;

    public EnemyPrototype at(int spawnX, int spawnY) {
        return new EnemyPrototype(this.name, this.healthPoints, this.speed, this.image, spawnX, spawnY);
    }
}
