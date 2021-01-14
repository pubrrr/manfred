package manfred.data.map;

import lombok.Value;
import manfred.data.map.matrix.MapMatrix;

import java.util.List;

@Value
public class ValidatedMapDto {
    String name;
    MapMatrix map;
    List<PersonDto> persons;
    List<TransporterDto> portals;
    List<TransporterDto> doors;
    List<EnemyDto> enemies;
}
