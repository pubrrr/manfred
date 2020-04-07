package manfred.game.map;

import manfred.game.GameFactory;
import manfred.game.exception.InvalidInputException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MapReader {
    public static final String PATH_MAPS = GameFactory.PATH_DATA + "maps\\";

    public String read(String jsonFileLocation) throws IOException {
        List<String> input = Files.readAllLines(Paths.get(jsonFileLocation));
        return String.join("", input);
    }

    public Map convert(String jsonString) throws InvalidInputException {
        try {
            JSONObject jsonInput = new JSONObject(jsonString);

            String name = jsonInput.getString("name");
            String[][] map = convertMap(jsonInput.getJSONArray("map"));

            return new Map(name, map);
        } catch (JSONException $e) {
            throw new InvalidInputException($e.getMessage());
        }
    }

    private String[][] convertMap(JSONArray jsonMap) throws InvalidInputException {
        int lengthVertical = jsonMap.length();
        int lengthHorizontal = jsonMap.getJSONArray(0).length();

        String[][] arrayMap = new String[lengthVertical][lengthHorizontal];
        for (int i = 0; i < lengthVertical; i++) {
            JSONArray horizontalLine = jsonMap.getJSONArray(i);
            if (horizontalLine.length() != lengthHorizontal) {
                throw new InvalidInputException("Map needs to be rectangular. First line: " + lengthHorizontal + ", line: " + i + " " + horizontalLine.length());
            }
            arrayMap[i] = convertHorizontalMapLine(horizontalLine);
        }
        return arrayMap;
    }

    private String[] convertHorizontalMapLine(JSONArray horizontalJsonLine) throws InvalidInputException {
        String[] arrayLine = new String[horizontalJsonLine.length()];
        for (int j = 0; j < horizontalJsonLine.length(); j++) {
            Object mapElement = horizontalJsonLine.get(j);
            if (mapElement instanceof String || mapElement instanceof Integer) {
                arrayLine[j] = "" + mapElement;
            } else {
                throw new InvalidInputException("Map array element was neither string nor int. Is: " + mapElement.toString());
            }
        }
        return arrayLine;
    }
}
