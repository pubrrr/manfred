package manfred.game.characters;

import manfred.game.graphics.ImageLoader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

public class ManfredFramesLoader {
    private final static String PATH_MANFRED_FRAMES = "data\\manfred\\";
    
    private ImageLoader imageLoader;

    public ManfredFramesLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public HashMap<Direction, BufferedImage[]> load() throws IOException {
        HashMap<Direction, BufferedImage[]> result = new HashMap<>();
        for (Direction direction : Direction.values()) {
            BufferedImage[] frames = new BufferedImage[Manfred.ANIMATION_IMAGES_NUMBER];

            for (int i = 0; i < Manfred.ANIMATION_IMAGES_NUMBER; i++) {
                frames[i] = imageLoader.load(PATH_MANFRED_FRAMES + "manfred_" + direction.toString() + i + ".png");
            }

            result.put(direction, frames);
        }
        return result;
    }

    public BufferedImage loadCastModeSprite() throws IOException {
        return imageLoader.load(PATH_MANFRED_FRAMES + "castModeSprite.png");
    }
}
