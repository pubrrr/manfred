package manfred.game.enemy;

import manfred.game.Game;
import manfred.game.exception.InvalidInputException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class EnemyReader {
    public static final String PATH_ENEMIES = Game.PATH_DATA + "enemies\\";
    private MapColliderProvider mapColliderProvider;

    public EnemyReader(MapColliderProvider mapColliderProvider) {
        this.mapColliderProvider = mapColliderProvider;
    }

    public Enemy load(String name, int spawnX, int spawnY) throws InvalidInputException, IOException {
        String jsonEnemy = read(PATH_ENEMIES + name + ".json");
        return convert(jsonEnemy, spawnX, spawnY);
    }

    String read(String jsonFileLocation) throws IOException {
        List<String> input = Files.readAllLines(Paths.get(jsonFileLocation));
        return String.join("", input);
    }

    Enemy convert(String jsonEnemy, int spawnX, int spawnY) throws InvalidInputException {
        try {
            JSONObject jsonInput = new JSONObject(jsonEnemy);

            String name = jsonInput.getString("name");
            int healthPoints = jsonInput.getInt("healthPoints");
            int speed = jsonInput.getInt("speed");

            return new Enemy(name, speed, spawnX, spawnY, healthPoints, mapColliderProvider.provide());
        } catch (Exception e) {
            throw new InvalidInputException(e.getMessage());
        }
    }
}
