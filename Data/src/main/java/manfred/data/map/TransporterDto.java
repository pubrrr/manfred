package manfred.data.map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransporterDto implements MapObjectDto {
    private String target;
    private int targetSpawnX;
    private int targetSpawnY;
    private int positionX;
    private int positionY;

    @Override
    public String getTargetToLoad() {
        return target;
    }
}
