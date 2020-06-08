package manfred.game.attack;

import manfred.game.Game;
import manfred.game.enemy.MapColliderProvider;
import manfred.game.exception.InvalidInputException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class AttackReader {

    public static final String PATH_ATTACKS = Game.PATH_DATA + "attacks\\";
    private MapColliderProvider mapColliderProvider;

    public AttackReader(MapColliderProvider mapColliderProvider) {
        this.mapColliderProvider = mapColliderProvider;
    }

    public AttackGenerator load(String name) throws InvalidInputException, IOException {
        String jsonAttack = read(PATH_ATTACKS + name + ".json");
        return convert(jsonAttack);
    }

    String read(String jsonFileLocation) throws IOException {
        List<String> input = Files.readAllLines(Paths.get(jsonFileLocation));
        return String.join("", input);
    }

    AttackGenerator convert(String jsonEnemy) throws InvalidInputException {
        try {
            JSONObject jsonInput = new JSONObject(jsonEnemy);

            int speed = jsonInput.getInt("speed");
            int sizeX = jsonInput.getInt("sizeX");
            int sizeY = jsonInput.getInt("sizeY");
            int damage = jsonInput.getInt("damage");
            int range = jsonInput.getInt("range");

            return new AttackGenerator(speed, sizeX, sizeY, mapColliderProvider.provide(), damage, range);
        } catch (Exception e) {
            throw new InvalidInputException(e.getMessage());
        }
    }
}
