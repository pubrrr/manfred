package manfred.game.map;

import manfred.data.DataContext;
import manfred.data.InvalidInputException;
import manfred.data.infrastructure.ObjectConverter;
import manfred.data.infrastructure.enemy.LocatedEnemyDto;
import manfred.data.infrastructure.map.ValidatedMapDto;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.game.config.GameConfig;
import manfred.game.conversion.map.TileConversionRule;
import manfred.game.enemy.EnemiesWrapper;
import manfred.game.enemy.Enemy;
import manfred.game.enemy.EnemyConverter;
import manfred.game.exception.ManfredException;
import manfred.game.interact.person.PersonProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

@Component
public class MapConverter implements ObjectConverter<ValidatedMapDto, Map> {
    public static final String ACCESSIBLE = "1";
    public static final String NOT_ACCESSIBLE = "0";
    public static final String PATH_MAPS = DataContext.PATH_DATA + "maps\\";
    public static final String PATH_MAPS_TILE_IMAGES = PATH_MAPS + "tiles\\";

    private final PersonProvider personProvider;
    private final EnemyConverter enemyConverter;
    private final EnemiesWrapper enemiesWrapper;
    private final GameConfig gameConfig;

    private final HashMap<String, MapTile> notAccessibleTilesStorage = new HashMap<>();

    private final TileConversionRule tileFactories;

    public MapConverter(PersonProvider personProvider, EnemyConverter enemyConverter, EnemiesWrapper enemiesWrapper, GameConfig gameConfig, TileConversionRule tileFactories) {
        this.personProvider = personProvider;
        this.enemyConverter = enemyConverter;
        this.enemiesWrapper = enemiesWrapper;
        this.gameConfig = gameConfig;
        this.tileFactories = tileFactories;
    }

    public Map convert(ValidatedMapDto input) {
        MapMatrix<TilePrototype> inputMap = input.getMap();
        int width = inputMap.sizeX();
        int height = inputMap.sizeY();

        List<List<MapTile>> resultingMapMatrix = createResultingMap(input, width, height);

        this.enemiesWrapper.setEnemies(convertEnemies(input.getEnemies()));

        return new Map(resultingMapMatrix, this.gameConfig);
    }

    private List<List<MapTile>> createResultingMap(ValidatedMapDto input, int width, int height) {
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

    private MapTile createMapTile(ValidatedMapDto input, int x, int y) {
        return this.tileFactories
            .applicableTo(input, x, y)
            .orElseThrow(runtimeException(input, x, y))
            .create();
    }

    private Supplier<RuntimeException> runtimeException(ValidatedMapDto input, int x, int y) {
        return () -> new RuntimeException("No applicable conversion rule for tile " + x + ", " + y + " in " + input.toString() + " found.");
    }

    Map convert(String jsonString) throws ManfredException, InvalidInputException {
        return null;
//        try {
//            JSONObject jsonInput = new JSONObject(jsonString);
//
//            String name = jsonInput.getString("name");
//            MapTile[][] mapTiles = convertMap(jsonInput.getJSONArray("map"));
//            convertAndInsertInteractables(jsonInput.optJSONArray("interactables"), mapTiles);
//
//            return new Map(mapTiles, gameConfig);
//        } catch (JSONException $e) {
//            throw new InvalidInputException($e.getMessage() + " in map " + jsonString);
//        }
    }

//    private MapTile[][] convertMap(JSONArray jsonMap) throws InvalidInputException {
//        int lengthVertical = jsonMap.length();
//        int lengthHorizontal = jsonMap.getJSONArray(0).length();
//
//        MapTile[][] transposedMapTiles = new MapTile[lengthVertical][lengthHorizontal];
//        for (int y = 0; y < lengthVertical; y++) {
//            JSONArray horizontalLine = jsonMap.getJSONArray(y);
//            if (horizontalLine.length() != lengthHorizontal) {
//                throw new InvalidInputException("Map needs to be rectangular. First line: " + lengthHorizontal + ", line: " + y + " " + horizontalLine.length());
//            }
//            transposedMapTiles[y] = convertHorizontalMapLine(horizontalLine);
//        }
//
//        return transposeToGetIntuitiveXAndYRight(transposedMapTiles, lengthVertical, lengthHorizontal);
//    }

//    private MapTile[][] transposeToGetIntuitiveXAndYRight(MapTile[][] original, int lengthVertical, int lengthHorizontal) {
//        MapTile[][] transposed = new MapTile[lengthHorizontal][lengthVertical];
//        for (int x = 0; x < lengthHorizontal; x++) {
//            for (int y = 0; y < lengthVertical; y++) {
//                transposed[x][y] = original[y][x];
//            }
//        }
//        return transposed;
//    }
//
//    private MapTile[] convertHorizontalMapLine(JSONArray horizontalJsonLine) throws InvalidInputException {
//        MapTile[] mapTileLine = new MapTile[horizontalJsonLine.length()];
//        for (int x = 0; x < horizontalJsonLine.length(); x++) {
//            Object tileValue = horizontalJsonLine.get(x);
//            if (tileValue instanceof String || tileValue instanceof Integer) {
//                mapTileLine[x] = convertMapTile("" + tileValue);
//            } else {
//                throw new InvalidInputException("Map array element was neither string nor int. Is: " + tileValue.toString());
//            }
//        }
//        return mapTileLine;
//    }

//    private MapTile convertMapTile(String tileValue) throws InvalidInputException {
//        if (tileValue.equals(ACCESSIBLE)) {
//            return Accessible.getInstance();
//        }
//
//        if (notAccessibleTilesStorage.containsKey(tileValue)) {
//            return notAccessibleTilesStorage.get(tileValue);
//        }
//
//        BufferedImage tileImage;
//        tileImage = imageLoader.load(PATH_MAPS_TILE_IMAGES + tileValue + ".png");
//
//        int blocksWidth;
//        int yOffset;
//        try {
//            String jsonTileConfig = textFileReader.read(PATH_MAPS_TILE_IMAGES + tileValue + ".json");
//            JSONObject tileConfig = new JSONObject(jsonTileConfig);
//            blocksWidth = tileConfig.optInt("blocksWidth", 1);
//            yOffset = tileConfig.optInt("yOffset", 0);
//        } catch (JSONException | InvalidInputException exception) {
//            blocksWidth = 1;
//            yOffset = 0;
//        }
//        NotAccessible notAccessibleTile = new NotAccessible(tileImage, gameConfig, blocksWidth, yOffset);
//        notAccessibleTilesStorage.put(tileValue, notAccessibleTile);
//        return notAccessibleTile;
//    }

//    private void convertAndInsertInteractables(@Nullable JSONArray interactables, MapTile[][] mapTiles) throws ManfredException, InvalidInputException {
//        if (interactables == null) {
//            return;
//        }
//        for (Object _jsonInteractable : interactables) {
//            if (!(_jsonInteractable instanceof JSONObject)) {
//                throw new InvalidInputException("Interactable was no JSON Object: " + _jsonInteractable.toString());
//            }
//            JSONObject jsonInteractable = (JSONObject) _jsonInteractable;
//
//            int positionX = jsonInteractable.getInt("positionX");
//            int positionY = jsonInteractable.getInt("positionY");
//            mapTiles[positionX][positionY] = convertInteractable(jsonInteractable);
//        }
//    }

//    private Interactable convertInteractable(JSONObject jsonInteractable) throws ManfredException, InvalidInputException {
//        String interactableType = jsonInteractable.getString("type");
//        return switch (interactableType) {
//            case "Person" -> personProvider.convert(
//                personReader.load(jsonInteractable.getString("name"))
//            );
//            case "Door" -> convertDoor(jsonInteractable);
//            case "Portal" -> convertPortal(jsonInteractable);
//            default -> throw new InvalidInputException("Unknown interactable type: " + interactableType + " for interactable " + jsonInteractable.toString());
//        };
//    }
//
//    private Door convertDoor(JSONObject interactable) {
//        return new Door(
//            interactable.getString("target"),
//            interactable.getInt("targetSpawnX"),
//            interactable.getInt("targetSpawnY"),
//            gameConfig
//        );
//    }
//
//    private Portal convertPortal(JSONObject interactable) {
//        return new Portal(
//            interactable.getString("target"),
//            interactable.getInt("targetSpawnX"),
//            interactable.getInt("targetSpawnY")
//        );
//    }

    private List<Enemy> convertEnemies(List<LocatedEnemyDto> enemies) {
        return enemies.stream()
            .map(enemyConverter::convert)
            .collect(toList());
    }

}
