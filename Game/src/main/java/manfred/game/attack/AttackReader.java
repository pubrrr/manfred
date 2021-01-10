package manfred.game.attack;

import manfred.game.Game;
import manfred.game.enemy.MapColliderProvider;
import manfred.game.exception.InvalidInputException;
import manfred.game.graphics.ImageLoader;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
public class AttackReader {

    public static final String PATH_ATTACKS = Game.PATH_DATA + "attacks\\";
    private MapColliderProvider mapColliderProvider;
    private ImageLoader imageLoader;

    public AttackReader(MapColliderProvider mapColliderProvider, ImageLoader imageLoader) {
        this.mapColliderProvider = mapColliderProvider;
        this.imageLoader = imageLoader;
    }

    public AttackGenerator load(String name) throws InvalidInputException, IOException {
        String jsonAttack = read(PATH_ATTACKS + name + ".json");
        return convert(jsonAttack);
    }

    String read(String jsonFileLocation) throws IOException {
        List<String> input = Files.readAllLines(Paths.get(jsonFileLocation));
        return String.join("", input);
    }

    AttackGenerator convert(String jsonAttack) throws InvalidInputException {
        try {
            JSONObject jsonInput = new JSONObject(jsonAttack);

            String name = jsonInput.getString("name");
            int speed = jsonInput.getInt("speed");
            int sizeX = jsonInput.getInt("sizeX");
            int sizeY = jsonInput.getInt("sizeY");
            int damage = jsonInput.getInt("damage");
            int range = jsonInput.getInt("range");
            int numberOfAnimationImages = jsonInput.getInt("numberOfAnimationImages");

            BufferedImage[] attackAnimation = loadAttackAnimation(name, numberOfAnimationImages);

            return new AttackGenerator(speed, sizeX, sizeY, mapColliderProvider.provide(), damage, range, attackAnimation, numberOfAnimationImages);
        } catch (Exception e) {
            throw new InvalidInputException(e.getMessage());
        }
    }

    private BufferedImage[] loadAttackAnimation(String name, int numberOfAnimationImages) throws IOException {
        BufferedImage[] attackAnimation = new BufferedImage[numberOfAnimationImages];
        for (int idx = 0; idx < numberOfAnimationImages; idx++) {
            attackAnimation[idx] = imageLoader.load(PATH_ATTACKS + name + "_" + idx + ".png");
        }
        return attackAnimation;
    }
}
