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
}
