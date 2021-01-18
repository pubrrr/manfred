package manfred.data.map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransporterDto extends MapObjectDto {
    private String target;
    private int targetSpawnX;
    private int targetSpawnY;

    public TransporterDto(String target, int targetSpawnX, int targetSpawnY, int positionX, int positionY) {
        this.target = target;
        this.targetSpawnX = targetSpawnX;
        this.targetSpawnY = targetSpawnY;
        this.setPositionX(positionX);
        this.setPositionY(positionY);
    }
}
