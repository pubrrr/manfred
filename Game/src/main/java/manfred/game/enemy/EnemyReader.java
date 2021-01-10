package manfred.game.enemy;

import manfred.data.DataContext;
import manfred.data.InvalidInputException;
import manfred.data.TextFileReader;
import manfred.data.image.ImageLoader;
import manfred.game.config.GameConfig;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class EnemyReader {
    public static final String PATH_ENEMIES = DataContext.PATH_DATA + "enemies\\";

    private final MapColliderProvider mapColliderProvider;
    private final ImageLoader imageLoader;
    private final GameConfig gameConfig;
    private final TextFileReader textFileReader;

    public EnemyReader(MapColliderProvider mapColliderProvider, ImageLoader imageLoader, GameConfig gameConfig, TextFileReader textFileReader) {
        this.mapColliderProvider = mapColliderProvider;
        this.imageLoader = imageLoader;
        this.gameConfig = gameConfig;
        this.textFileReader = textFileReader;
    }

    public Enemy load(String name, int spawnX, int spawnY) throws InvalidInputException {
        String jsonEnemy = textFileReader.read(PATH_ENEMIES + name + ".json");
        return convert(jsonEnemy, spawnX, spawnY);
    }

    Enemy convert(String jsonEnemy, int spawnX, int spawnY) throws InvalidInputException {
        try {
            JSONObject jsonInput = new JSONObject(jsonEnemy);

            String name = jsonInput.getString("name");
            int healthPoints = jsonInput.getInt("healthPoints");
            int speed = jsonInput.getInt("speed");

            return new Enemy(
                    name,
                    speed,
                    gameConfig.getPixelBlockSize() * spawnX,
                    gameConfig.getPixelBlockSize() * spawnY,
                    healthPoints,
                    imageLoader.load(PATH_ENEMIES + name + ".png"),
                    mapColliderProvider.provide(),
                    gameConfig.getPixelBlockSize() * 5,
                    gameConfig
            );
        } catch (Exception e) {
            throw new InvalidInputException(e.getMessage());
        }
    }
}
