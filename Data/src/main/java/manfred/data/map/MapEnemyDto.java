package manfred.data.map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapEnemyDto implements MapObjectDto {
    private String name;
    private int positionX;
    private int positionY;

    @Override
    public String getTargetToLoad() {
        return name;
    }
}
