package manfred.infrastructureadapter.map;

import manfred.data.infrastructure.ObjectConverter;
import manfred.data.infrastructure.enemy.EnemyPrototype;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.game.enemy.EnemiesWrapper;
import manfred.game.enemy.Enemy;
import manfred.game.map.Map;
import manfred.game.map.MapTile;
import manfred.infrastructureadapter.enemy.EnemyConverter;
import manfred.data.infrastructure.map.TileConversionRule;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Component
public class MapConverter implements ObjectConverter<MapPrototype, Map> {

    private final EnemyConverter enemyConverter;
    private final EnemiesWrapper enemiesWrapper;

    private final TileConversionRule<MapTile> tileFactories;

    public MapConverter(EnemyConverter enemyConverter, EnemiesWrapper enemiesWrapper, TileConversionRule<MapTile> tileFactories) {
        this.enemyConverter = enemyConverter;
        this.enemiesWrapper = enemiesWrapper;
        this.tileFactories = tileFactories;
    }

    public Map convert(MapPrototype input) {
        java.util.Map<MapPrototype.Coordinate, MapTile> mapTilesByCoordinate = input.getCoordinateSet().stream()
            .collect(toMap(
                coordinate -> coordinate,
                coordinate -> createMapTile(input, coordinate)
            ));

        Map map = new Map(mapTilesByCoordinate);
        this.enemiesWrapper.setEnemies(convertEnemies(input.getEnemies(), map));
        return map;
    }

    private MapTile createMapTile(MapPrototype input, MapPrototype.Coordinate coordinate) {
        return this.tileFactories
            .applicableTo(input, coordinate)
            .orElseThrow(runtimeException(input, coordinate))
            .create();
    }

    private Supplier<RuntimeException> runtimeException(MapPrototype input, MapPrototype.Coordinate coordinate) {
        return () -> new RuntimeException("No applicable conversion rule for tile " + coordinate.getX() + ", " + coordinate.getY() + " in " + input.toString() + " found.");
    }

    private List<Enemy> convertEnemies(List<EnemyPrototype> enemies, Map map) {
        return enemies.stream()
            .map(enemyConverter::convert)
            .map(enemyFactory -> enemyFactory.createOnMap(map))
            .collect(toList());
    }
}
