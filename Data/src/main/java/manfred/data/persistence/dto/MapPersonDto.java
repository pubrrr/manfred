package manfred.data.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import manfred.data.shared.PositiveInt;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapPersonDto implements MapObjectDto {
    private String name;
    private PositiveInt positionX;
    private PositiveInt positionY;

    @Override
    public String getTargetToLoad() {
        return name;
    }
}
