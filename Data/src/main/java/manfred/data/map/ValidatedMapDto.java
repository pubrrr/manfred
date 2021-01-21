package manfred.data.map;

import lombok.Value;
import manfred.data.enemy.LocatedEnemyDto;
import manfred.data.map.matrix.MapMatrix;
import manfred.data.map.tile.TilePrototype;
import manfred.data.person.LocatedPersonDto;

import java.util.List;

@Value
public class ValidatedMapDto {
    String name;
    MapMatrix<TilePrototype> map;
    List<LocatedPersonDto> persons;
    List<TransporterDto> portals;
    List<TransporterDto> doors;
    List<LocatedEnemyDto> enemies;
}
