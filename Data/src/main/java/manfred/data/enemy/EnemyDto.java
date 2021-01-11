package manfred.data.enemy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnemyDto {
    private String name;
    private int healthPoints;
    private int speed;

    @JsonIgnore
    BufferedImage image;
}
