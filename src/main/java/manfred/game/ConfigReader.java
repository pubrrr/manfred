package manfred.game;

import manfred.game.exception.InvalidInputException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ConfigReader {
    private final static String PATH_CONFIG = Game.PATH_DATA + "config\\";

    public GameConfig load() throws InvalidInputException, IOException {
        String jsonConfig = read(PATH_CONFIG + "config.json");
        return convert(jsonConfig);
    }

    String read(String jsonFileLocation) throws IOException {
        List<String> input = Files.readAllLines(Paths.get(jsonFileLocation));
        return String.join("", input);
    }

    GameConfig convert(String jsonEnemy) throws InvalidInputException {
        try {
            JSONObject jsonInput = new JSONObject(jsonEnemy);

            JSONObject window = jsonInput.getJSONObject("window");
            JSONObject gelaber = jsonInput.getJSONObject("gelaber");

            return new GameConfig(
                    window.getInt("width"),
                    window.getInt("height"),
                    jsonInput.getInt("pixelBlockSize"),
                    jsonInput.getInt("textBoxDistanceToBorder"),
                    jsonInput.getInt("textPointSize"),
                    jsonInput.getInt("textDistanceToBox"),
                    gelaber.getInt("boxPositionX"),
                    gelaber.getInt("boxPositionY")
            );
        } catch (Exception e) {
            throw new InvalidInputException(e.getMessage());
        }
    }
}
