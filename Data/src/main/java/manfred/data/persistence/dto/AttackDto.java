package manfred.data.persistence.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttackDto {
    private String name;
    private int speed;
    private int sizeX;
    private int sizeY;
    private int damage;
    private int range;
    private int numberOfAnimationImages;
    @JsonIgnore private List<BufferedImage> attackAnimation;
}
