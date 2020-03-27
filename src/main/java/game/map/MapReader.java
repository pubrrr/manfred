package game.map;

import org.json.*;

public class MapReader {
    public void read(String fileLocation) {
//        JSONObject
    }

    public Map convert(String jsonString) {
        JSONObject jsonInput = new JSONObject(jsonString);

        String name = jsonInput.getString("name");
        String[][] map = new String[0][0];
        return new Map(name, map);
    }

}
