package manfred.game.map;

import manfred.game.Game;
import manfred.game.exception.InvalidInputException;
import manfred.game.interact.Door;
import manfred.game.interact.Interactable;
import manfred.game.interact.PersonReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MapReader {
    public static final String ACCESSIBLE = "1";
    public static final String NOT_ACCESSIBLE = "0";
    public static final String PATH_MAPS = Game.PATH_DATA + "maps\\";

    private PersonReader personReader;

    public MapReader(PersonReader personReader) {
        this.personReader = personReader;
    }

    public Map load(String name) throws InvalidInputException, IOException {
        String jsonMap = read(PATH_MAPS + name + ".json");
        return convert(jsonMap);
    }

    String read(String jsonFileLocation) throws IOException {
        List<String> input = Files.readAllLines(Paths.get(jsonFileLocation));
        return String.join("", input);
    }

    Map convert(String jsonString) throws InvalidInputException {
        try {
            JSONObject jsonInput = new JSONObject(jsonString);

            String name = jsonInput.getString("name");
            MapTile[][] mapTiles = convertMap(jsonInput.getJSONArray("map"), jsonInput.getJSONObject("interactables"));

            return new Map(name, mapTiles);
        } catch (JSONException | IOException $e) {
            throw new InvalidInputException($e.getMessage());
        }
    }

    private MapTile[][] convertMap(JSONArray jsonMap, JSONObject jsonInteractables) throws InvalidInputException, IOException {
        int lengthVertical = jsonMap.length();
        int lengthHorizontal = jsonMap.getJSONArray(0).length();

        MapTile[][] transposedMapTiles = new MapTile[lengthVertical][lengthHorizontal];
        for (int y = 0; y < lengthVertical; y++) {
            JSONArray horizontalLine = jsonMap.getJSONArray(y);
            if (horizontalLine.length() != lengthHorizontal) {
                throw new InvalidInputException("Map needs to be rectangular. First line: " + lengthHorizontal + ", line: " + y + " " + horizontalLine.length());
            }
            transposedMapTiles[y] = convertHorizontalMapLine(horizontalLine, jsonInteractables);
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

    private MapTile[] convertHorizontalMapLine(JSONArray horizontalJsonLine, JSONObject jsonInteractables) throws InvalidInputException, IOException {
        MapTile[] mapTileLine = new MapTile[horizontalJsonLine.length()];
        for (int x = 0; x < horizontalJsonLine.length(); x++) {
            Object tileValue = horizontalJsonLine.get(x);
            if (tileValue instanceof String || tileValue instanceof Integer) {
                mapTileLine[x] = convertMapTile("" + tileValue, jsonInteractables);
            } else {
                throw new InvalidInputException("Map array element was neither string nor int. Is: " + tileValue.toString());
            }
        }
        return mapTileLine;
    }

    private MapTile convertMapTile(String tileValue, JSONObject jsonInteractables) throws InvalidInputException, IOException {
        switch (tileValue) {
            case ACCESSIBLE:
                return new Accessible();
            case NOT_ACCESSIBLE:
                return new NotAccessible();
            default:
                return convertInteractable(tileValue, jsonInteractables);
        }
    }

    private Interactable convertInteractable(String interactableId, JSONObject jsonInteractables) throws InvalidInputException, IOException {
        JSONObject interactable = jsonInteractables.getJSONObject(interactableId);
        String interactableType = interactable.getString("type");
        switch (interactableType) {
            case "Person":
                return personReader.load(interactableId);
            case "Door":
                return convertDoor(interactableId, interactable);
            default:
                throw new InvalidInputException("Unknown interactable type: " + interactableType + " for interactable " + interactableId);
        }
    }

    private Door convertDoor(String targetName, JSONObject interactable) {
        return new Door(targetName, interactable.getInt("targetSpawnX"), interactable.getInt("targetSpawnY"));
    }
}
