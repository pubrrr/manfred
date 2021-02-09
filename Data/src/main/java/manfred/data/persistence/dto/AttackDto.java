package manfred.data.persistence.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import manfred.data.shared.PositiveInt;

import java.awt.image.BufferedImage;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttackDto {
    private String name;
    private PositiveInt speed;
    private PositiveInt sizeX;
    private PositiveInt sizeY;
    private PositiveInt damage;
    private PositiveInt range;
    private PositiveInt numberOfAnimationImages;
    @JsonIgnore private List<BufferedImage> attackAnimation;
}
