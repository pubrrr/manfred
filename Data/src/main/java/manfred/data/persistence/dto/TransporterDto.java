package manfred.data.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import manfred.data.shared.PositiveInt;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransporterDto implements MapObjectDto {
    private String target;
    private PositiveInt targetSpawnX;
    private PositiveInt targetSpawnY;
    private PositiveInt positionX;
    private PositiveInt positionY;

    @Override
    public String getTargetToLoad() {
        return target;
    }
}
