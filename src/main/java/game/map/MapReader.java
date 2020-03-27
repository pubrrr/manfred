package game.map;

import game.exception.InvalidInputException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.ws.BindingType;

public class MapReader {
    public void read(String fileLocation) {
//        JSONObject
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
