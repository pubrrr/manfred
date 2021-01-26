package manfred.game.characters;

import manfred.data.InvalidInputException;
import manfred.data.persistence.reader.ManfredFrameLoader;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.HashMap;

@Component
public class ManfredFramesLoader {

    private final ManfredFrameLoader manfredFrameLoader;

    public ManfredFramesLoader(ManfredFrameLoader manfredFrameLoader) {
        this.manfredFrameLoader = manfredFrameLoader;
    }

    public HashMap<Direction, BufferedImage[]> loadWalkAnimation() throws InvalidInputException {
        HashMap<Direction, BufferedImage[]> walkAnimation = new HashMap<>();
        for (Direction direction : Direction.values()) {
            BufferedImage[] frames = new BufferedImage[Manfred.ANIMATION_IMAGES_NUMBER];

            for (int i = 0; i < Manfred.ANIMATION_IMAGES_NUMBER; i++) {
                frames[i] = manfredFrameLoader.loadFrame("manfred_" + direction.toString().toLowerCase() + i);
            }

            walkAnimation.put(direction, frames);
        }
        return walkAnimation;
    }

    public BufferedImage loadCastModeSprite() throws InvalidInputException {
        return manfredFrameLoader.loadFrame("castModeSprite");
    }
}
