package manfred.game.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageLoader {
    public BufferedImage load(String source) throws IOException {
        File file = new File(source);
        return ImageIO.read(file);
    }
}
