package manfred.data.infrastructure.map;

import lombok.Value;
import manfred.data.infrastructure.enemy.LocatedEnemyDto;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.data.infrastructure.person.LocatedPersonDto;
import manfred.data.persistence.dto.TransporterDto;

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
