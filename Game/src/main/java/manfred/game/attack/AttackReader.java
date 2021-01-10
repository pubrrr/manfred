package manfred.game.attack;

import manfred.data.DataContext;
import manfred.data.InvalidInputException;
import manfred.data.TextFileReader;
import manfred.data.image.ImageLoader;
import manfred.game.enemy.MapColliderProvider;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
public class AttackReader {

    public static final String PATH_ATTACKS = DataContext.PATH_DATA + "attacks\\";
    private final MapColliderProvider mapColliderProvider;
    private final ImageLoader imageLoader;
    private final TextFileReader textFileReader;

    public AttackReader(MapColliderProvider mapColliderProvider, ImageLoader imageLoader, TextFileReader textFileReader) {
        this.mapColliderProvider = mapColliderProvider;
        this.imageLoader = imageLoader;
        this.textFileReader = textFileReader;
    }

    public AttackGenerator load(String name) throws InvalidInputException {
        String jsonAttack = textFileReader.read(PATH_ATTACKS + name + ".json");
        return convert(jsonAttack);
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

    private BufferedImage[] loadAttackAnimation(String name, int numberOfAnimationImages) throws InvalidInputException {
        BufferedImage[] attackAnimation = new BufferedImage[numberOfAnimationImages];
        for (int idx = 0; idx < numberOfAnimationImages; idx++) {
            attackAnimation[idx] = imageLoader.load(PATH_ATTACKS + name + "_" + idx + ".png");
        }
        return attackAnimation;
    }
}
