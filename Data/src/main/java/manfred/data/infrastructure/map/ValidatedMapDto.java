package manfred.data.infrastructure.map;

import lombok.Value;
import manfred.data.infrastructure.enemy.LocatedEnemyDto;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.data.infrastructure.person.PersonPrototype;
import manfred.data.persistence.dto.TransporterDto;

import java.util.List;

@Value
public class ValidatedMapDto {
    String name;
    MapMatrix<TilePrototype> map;
    List<PersonPrototype> persons;
    List<TransporterDto> portals;
    List<TransporterDto> doors;
    List<LocatedEnemyDto> enemies;
}
