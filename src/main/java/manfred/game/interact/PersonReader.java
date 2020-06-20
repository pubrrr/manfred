package manfred.game.interact;

import manfred.game.Game;
import manfred.game.GameConfig;
import manfred.game.exception.InvalidInputException;
import manfred.game.interact.gelaber.Gelaber;
import manfred.game.interact.gelaber.GelaberReader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class PersonReader {
    public static final String PATH_PERSONS = Game.PATH_DATA + "persons\\";

    private GelaberReader gelaberReader;
    private GameConfig gameConfig;

    public PersonReader(GelaberReader gelaberReader, GameConfig gameConfig) {
        this.gelaberReader = gelaberReader;
        this.gameConfig = gameConfig;
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

            return new Person(name, gelaber, gameConfig);
        } catch (JSONException e) {
            throw new InvalidInputException(e.getMessage());
        }
    }
}
