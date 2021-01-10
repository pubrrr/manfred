package manfred.data.image;

import manfred.data.InvalidInputException;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

@Component
public class ImageLoader {
    public BufferedImage load(String source) throws InvalidInputException {
        File file = new File(source);
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            throw new InvalidInputException(e);
        }
    }

    public BufferedImage load(URL source) throws InvalidInputException {
        try {
            return ImageIO.read(source);
        } catch (IOException e) {
            throw new InvalidInputException(e);
        }
    }
}
