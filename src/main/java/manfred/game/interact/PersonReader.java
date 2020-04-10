package manfred.game.interact;

import manfred.game.Game;
import manfred.game.exception.InvalidInputException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class PersonReader {
    public static final String PATH_PERSONS = Game.PATH_DATA + "persons\\";

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

            return new Person();
        } catch (JSONException $e) {
            throw new InvalidInputException($e.getMessage());
        }
    }
}
