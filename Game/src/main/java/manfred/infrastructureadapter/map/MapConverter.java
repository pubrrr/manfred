package manfred.infrastructureadapter.map;

import manfred.data.infrastructure.ObjectConverter;
import manfred.data.infrastructure.enemy.EnemyPrototype;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.game.enemy.EnemiesWrapper;
import manfred.game.enemy.Enemy;
import manfred.game.map.Map;
import manfred.game.map.MapTile;
import manfred.infrastructureadapter.enemy.EnemyConverter;
import manfred.infrastructureadapter.map.tile.TileConversionRule;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

@Component
public class MapConverter implements ObjectConverter<MapPrototype, Map> {

    private final EnemyConverter enemyConverter;
    private final EnemiesWrapper enemiesWrapper;

    private final TileConversionRule tileFactories;

    public MapConverter(EnemyConverter enemyConverter, EnemiesWrapper enemiesWrapper, TileConversionRule tileFactories) {
        this.enemyConverter = enemyConverter;
        this.enemiesWrapper = enemiesWrapper;
        this.tileFactories = tileFactories;
    }

    public Map convert(MapPrototype input) {
        MapMatrix<TilePrototype> inputMap = input.getMap();
        int width = inputMap.sizeX();
        int height = inputMap.sizeY();

        List<List<MapTile>> resultingMapMatrix = createResultingMap(input, width, height);

        Map map = new Map(resultingMapMatrix);
        this.enemiesWrapper.setEnemies(convertEnemies(input.getEnemies(), map));
        return map;
    }

    private List<List<MapTile>> createResultingMap(MapPrototype input, int width, int height) {
        List<List<MapTile>> resultingMap = new ArrayList<>(width);
        for (int x = 0; x < width; x++) {
            List<MapTile> column = new ArrayList<>(height);
            for (int y = 0; y < height; y++) {
                column.add(createMapTile(input, x, y));
            }
            resultingMap.add(column);
        }
        return resultingMap;
    }

    private MapTile createMapTile(MapPrototype input, int x, int y) {
        return this.tileFactories
            .applicableTo(input, x, y)
            .orElseThrow(runtimeException(input, x, y))
            .create();
    }

    private Supplier<RuntimeException> runtimeException(MapPrototype input, int x, int y) {
        return () -> new RuntimeException("No applicable conversion rule for tile " + x + ", " + y + " in " + input.toString() + " found.");
    }

    private List<Enemy> convertEnemies(List<EnemyPrototype> enemies, Map map) {
        return enemies.stream()
            .map(enemyConverter::convert)
            .map(enemyFactory -> enemyFactory.createOnMap(map))
            .collect(toList());
    }
}
