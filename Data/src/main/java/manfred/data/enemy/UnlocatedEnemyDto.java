package manfred.data.enemy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnlocatedEnemyDto {
    private String name;
    private int healthPoints;
    private int speed;

    @JsonIgnore
    private BufferedImage image;

    public EnemyDto at(int spawnX, int spawnY) {
        return new EnemyDto(this.name, this.healthPoints, this.speed, this.image, spawnX, spawnY);
    }
}
