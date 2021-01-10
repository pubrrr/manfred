package manfred.game.characters;

import manfred.data.DataContext;
import manfred.data.InvalidInputException;
import manfred.data.image.ImageLoader;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.HashMap;

@Component
public class ManfredFramesLoader {
    private final static String PATH_MANFRED_FRAMES = DataContext.PATH_DATA + "manfred\\";
    
    private final ImageLoader imageLoader;

    public ManfredFramesLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public HashMap<Direction, BufferedImage[]> loadWalkAnimation() throws InvalidInputException {
        HashMap<Direction, BufferedImage[]> walkAnimation = new HashMap<>();
        for (Direction direction : Direction.values()) {
            BufferedImage[] frames = new BufferedImage[Manfred.ANIMATION_IMAGES_NUMBER];

            for (int i = 0; i < Manfred.ANIMATION_IMAGES_NUMBER; i++) {
                frames[i] = imageLoader.load(PATH_MANFRED_FRAMES + "manfred_" + direction.toString().toLowerCase() + i + ".png");
            }

            walkAnimation.put(direction, frames);
        }
        return walkAnimation;
    }

    public BufferedImage loadCastModeSprite() throws InvalidInputException {
        return imageLoader.load(PATH_MANFRED_FRAMES + "castModeSprite.png");
    }
}
