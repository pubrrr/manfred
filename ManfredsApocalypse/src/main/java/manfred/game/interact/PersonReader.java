package manfred.game.interact;

import manfred.game.Game;
import manfred.game.GameConfig;
import manfred.game.exception.InvalidInputException;
import manfred.game.graphics.ImageLoader;
import manfred.game.interact.gelaber.Gelaber;
import manfred.game.interact.gelaber.GelaberReader;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
public class PersonReader {
    public static final String PATH_PERSONS = Game.PATH_DATA + "persons\\";

    private GelaberReader gelaberReader;
    private GameConfig gameConfig;
    private ImageLoader imageLoader;

    public PersonReader(GelaberReader gelaberReader, GameConfig gameConfig, ImageLoader imageLoader) {
        this.gelaberReader = gelaberReader;
        this.gameConfig = gameConfig;
        this.imageLoader = imageLoader;
    }

    public Person load(String name) throws InvalidInputException, IOException {
        String jsonPerson = read(PATH_PERSONS + name + ".json");
        return convert(jsonPerson);
    }

    String read(String jsonFileLocation) throws IOException {
        List<String> input = Files.readAllLines(Paths.get(jsonFileLocation));
        return String.join("", input);
    }

    Person convert(String jsonString) throws InvalidInputException {
        try {
            JSONObject jsonInput = new JSONObject(jsonString);

            String name = jsonInput.getString("name");
            Gelaber gelaber = gelaberReader.convert(jsonInput.getJSONArray("gelaber"));
            BufferedImage image = imageLoader.load(PATH_PERSONS + name + ".png");

            return new Person(name, gelaber, gameConfig, image);
        } catch (JSONException | IOException e) {
            throw new InvalidInputException(e.getMessage());
        }
    }
}
