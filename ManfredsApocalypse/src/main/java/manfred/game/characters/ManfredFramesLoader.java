package manfred.game.characters;

import manfred.game.Game;
import manfred.game.graphics.ImageLoader;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

@Component
public class ManfredFramesLoader {
    private final static String PATH_MANFRED_FRAMES = Game.PATH_DATA + "manfred\\";
    
    private final ImageLoader imageLoader;

    public ManfredFramesLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public HashMap<Direction, BufferedImage[]> loadWalkAnimation() throws IOException {
        HashMap<Direction, BufferedImage[]> walkAnimation = new HashMap<>();
        for (Direction direction : Direction.values()) {
            BufferedImage[] frames = new BufferedImage[Manfred.ANIMATION_IMAGES_NUMBER];

            for (int i = 0; i < Manfred.ANIMATION_IMAGES_NUMBER; i++) {
                frames[i] = imageLoader.load(PATH_MANFRED_FRAMES + "manfred_" + direction.toString() + i + ".png");
            }

            walkAnimation.put(direction, frames);
        }
        return walkAnimation;
    }

    public BufferedImage loadCastModeSprite() throws IOException {
        return imageLoader.load(PATH_MANFRED_FRAMES + "castModeSprite.png");
    }
}
