package manfred.game.map;

import manfred.game.Game;
import manfred.game.exception.InvalidInputException;
import manfred.game.interact.Interact;
import manfred.game.interact.PersonReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class MapReader {
    public static final String PATH_MAPS = Game.PATH_DATA + "maps\\";

    private PersonReader personReader;

    private Stack<String> interactsFoundInMap;

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
        this.interactsFoundInMap = new Stack<>();

        try {
            JSONObject jsonInput = new JSONObject(jsonString);

            String name = jsonInput.getString("name");
            String[][] map = convertMap(jsonInput.getJSONArray("map"));
            HashMap<String, Interact> interacts = new HashMap<>();
            if (!interactsFoundInMap.empty()) {
                interacts = convertInteracts(jsonInput.getJSONObject("interacts"));
            }

            return new Map(name, map, interacts);
        } catch (JSONException | IOException $e) {
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
                rememberInteract(arrayLine[j]);
            } else {
                throw new InvalidInputException("Map array element was neither string nor int. Is: " + mapElement.toString());
            }
        }
        return arrayLine;
    }

    private void rememberInteract(String mapElement) {
        if (!mapElement.equals("1") && !mapElement.equals("0")) {
            interactsFoundInMap.push(mapElement);
        }
    }

    private HashMap<String, Interact> convertInteracts(JSONObject jsonInteracts) throws InvalidInputException, IOException {
        HashMap<String, Interact> result = new HashMap<>(interactsFoundInMap.size());

        while (!interactsFoundInMap.empty()) {
            String interactId = interactsFoundInMap.pop();
            String interactType = jsonInteracts.getString(interactId);
            if (interactType.equals("Person")) {
                result.put(interactId, personReader.load(interactId));
            }
        }
        return result;
    }
}
