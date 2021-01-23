package manfred.data.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapPersonDto implements MapObjectDto {
    private String name;
    private int positionX;
    private int positionY;

    @Override
    public String getTargetToLoad() {
        return name;
    }
}
