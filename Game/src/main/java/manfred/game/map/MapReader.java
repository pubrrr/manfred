package manfred.game.map;

import manfred.data.DataContext;
import manfred.data.InvalidInputException;
import manfred.data.TextFileReader;
import manfred.data.image.ImageLoader;
import manfred.data.person.PersonReader;
import manfred.game.config.GameConfig;
import manfred.game.enemy.EnemiesWrapper;
import manfred.game.enemy.EnemyReader;
import manfred.game.enemy.EnemyStack;
import manfred.game.exception.ManfredException;
import manfred.game.interact.Door;
import manfred.game.interact.Interactable;
import manfred.game.interact.Portal;
import manfred.game.interact.person.PersonConverter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

@Component
public class MapReader {
    public static final String ACCESSIBLE = "1";
    public static final String NOT_ACCESSIBLE = "0";
    public static final String PATH_MAPS = DataContext.PATH_DATA + "maps\\";
    public static final String PATH_MAPS_TILE_IMAGES = PATH_MAPS + "tiles\\";

    private final PersonConverter personConverter;
    private final EnemyReader enemyReader;
    private final EnemiesWrapper enemiesWrapper;
    private final GameConfig gameConfig;
    private final ImageLoader imageLoader;
    private final TextFileReader textFileReader;
    private final PersonReader personReader;

    private final HashMap<String, MapTile> notAccessibleTilesStorage = new HashMap<>();

    public MapReader(PersonConverter personConverter, EnemyReader enemyReader, EnemiesWrapper enemiesWrapper, GameConfig gameConfig, ImageLoader imageLoader, TextFileReader textFileReader, PersonReader personReader) {
        this.personConverter = personConverter;
        this.enemyReader = enemyReader;
        this.enemiesWrapper = enemiesWrapper;
        this.gameConfig = gameConfig;
        this.imageLoader = imageLoader;
        this.textFileReader = textFileReader;
        this.personReader = personReader;
    }

    public Map load(String name) throws ManfredException, InvalidInputException {
        String jsonMap = textFileReader.read(PATH_MAPS + name + ".json");
        return convert(jsonMap);
    }

    String read(String jsonFileLocation) throws IOException {
        List<String> input = Files.readAllLines(Paths.get(jsonFileLocation));
        return String.join("", input);
    }

    Map convert(String jsonString) throws ManfredException, InvalidInputException {
        try {
            JSONObject jsonInput = new JSONObject(jsonString);

            String name = jsonInput.getString("name");
            MapTile[][] mapTiles = convertMap(jsonInput.getJSONArray("map"));
            convertAndInsertInteractables(jsonInput.optJSONArray("interactables"), mapTiles);
            EnemyStack enemies = convertEnemies(jsonInput.optJSONArray("enemies"));
            enemiesWrapper.setEnemies(enemies);

            return new Map(name, mapTiles, gameConfig);
        } catch (JSONException $e) {
            throw new InvalidInputException($e.getMessage() + " in map " + jsonString);
        }
    }

    private MapTile[][] convertMap(JSONArray jsonMap) throws InvalidInputException {
        int lengthVertical = jsonMap.length();
        int lengthHorizontal = jsonMap.getJSONArray(0).length();

        MapTile[][] transposedMapTiles = new MapTile[lengthVertical][lengthHorizontal];
        for (int y = 0; y < lengthVertical; y++) {
            JSONArray horizontalLine = jsonMap.getJSONArray(y);
            if (horizontalLine.length() != lengthHorizontal) {
                throw new InvalidInputException("Map needs to be rectangular. First line: " + lengthHorizontal + ", line: " + y + " " + horizontalLine.length());
            }
            transposedMapTiles[y] = convertHorizontalMapLine(horizontalLine);
        }

        return transposeToGetIntuitiveXAndYRight(transposedMapTiles, lengthVertical, lengthHorizontal);
    }

    private MapTile[][] transposeToGetIntuitiveXAndYRight(MapTile[][] original, int lengthVertical, int lengthHorizontal) {
        MapTile[][] transposed = new MapTile[lengthHorizontal][lengthVertical];
        for (int x = 0; x < lengthHorizontal; x++) {
            for (int y = 0; y < lengthVertical; y++) {
                transposed[x][y] = original[y][x];
            }
        }
        return transposed;
    }

    private MapTile[] convertHorizontalMapLine(JSONArray horizontalJsonLine) throws InvalidInputException {
        MapTile[] mapTileLine = new MapTile[horizontalJsonLine.length()];
        for (int x = 0; x < horizontalJsonLine.length(); x++) {
            Object tileValue = horizontalJsonLine.get(x);
            if (tileValue instanceof String || tileValue instanceof Integer) {
                mapTileLine[x] = convertMapTile("" + tileValue);
            } else {
                throw new InvalidInputException("Map array element was neither string nor int. Is: " + tileValue.toString());
            }
        }
        return mapTileLine;
    }

    private MapTile convertMapTile(String tileValue) throws InvalidInputException {
        if (tileValue.equals(ACCESSIBLE)) {
            return Accessible.getInstance();
        }

        if (notAccessibleTilesStorage.containsKey(tileValue)) {
            return notAccessibleTilesStorage.get(tileValue);
        }

        BufferedImage tileImage;
        tileImage = imageLoader.load(PATH_MAPS_TILE_IMAGES + tileValue + ".png");

        int blocksWidth;
        int yOffset;
        try {
            String jsonTileConfig = textFileReader.read(PATH_MAPS_TILE_IMAGES + tileValue + ".json");
            JSONObject tileConfig = new JSONObject(jsonTileConfig);
            blocksWidth = tileConfig.optInt("blocksWidth", 1);
            yOffset = tileConfig.optInt("yOffset", 0);
        } catch (JSONException exception) {
            blocksWidth = 1;
            yOffset = 0;
        }
        NotAccessible notAccessibleTile = new NotAccessible(tileImage, gameConfig, blocksWidth, yOffset);
        notAccessibleTilesStorage.put(tileValue, notAccessibleTile);
        return notAccessibleTile;
    }

    private void convertAndInsertInteractables(@Nullable JSONArray interactables, MapTile[][] mapTiles) throws ManfredException, InvalidInputException {
        if (interactables == null) {
            return;
        }
        for (Object _jsonInteractable : interactables) {
            if (!(_jsonInteractable instanceof JSONObject)) {
                throw new InvalidInputException("Interactable was no JSON Object: " + _jsonInteractable.toString());
            }
            JSONObject jsonInteractable = (JSONObject) _jsonInteractable;

            int positionX = jsonInteractable.getInt("positionX");
            int positionY = jsonInteractable.getInt("positionY");
            mapTiles[positionX][positionY] = convertInteractable(jsonInteractable);
        }
    }

    private Interactable convertInteractable(JSONObject jsonInteractable) throws ManfredException, InvalidInputException {
        String interactableType = jsonInteractable.getString("type");
        return switch (interactableType) {
            case "Person" -> personConverter.convert(
                personReader.load(jsonInteractable.getString("name"))
            );
            case "Door" -> convertDoor(jsonInteractable);
            case "Portal" -> convertPortal(jsonInteractable);
            default -> throw new InvalidInputException("Unknown interactable type: " + interactableType + " for interactable " + jsonInteractable.toString());
        };
    }

    private Door convertDoor(JSONObject interactable) {
        return new Door(
            interactable.getString("target"),
            interactable.getInt("targetSpawnX"),
            interactable.getInt("targetSpawnY"),
            gameConfig
        );
    }

    private Portal convertPortal(JSONObject interactable) {
        return new Portal(
            interactable.getString("target"),
            interactable.getInt("targetSpawnX"),
            interactable.getInt("targetSpawnY")
        );
    }

    private EnemyStack convertEnemies(@Nullable JSONArray enemies) throws InvalidInputException {
        EnemyStack enemyStack = new EnemyStack();
        if (enemies == null) {
            return enemyStack;
        }

        for (Object enemy : enemies) {
            if (!(enemy instanceof JSONObject)) {
                throw new InvalidInputException("Enemy was not a JSONObject: " + enemy.toString());
            }
            String name = ((JSONObject) enemy).getString("name");
            int spawnX = ((JSONObject) enemy).getInt("spawnX");
            int spawnY = ((JSONObject) enemy).getInt("spawnY");
            enemyStack.add(enemyReader.load(name, spawnX, spawnY));
        }
        return enemyStack;
    }
}
