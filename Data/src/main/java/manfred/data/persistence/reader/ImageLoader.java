package manfred.data.persistence.reader;

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
        return load((new File(source)).getAbsoluteFile());
    }

    public BufferedImage load(URL source) throws InvalidInputException {
        try {
            return ImageIO.read(source);
        } catch (IOException | IllegalArgumentException e) {
            throw new InvalidInputException("Could not read from " + source, e);
        }
    }

    public BufferedImage load(File file) throws InvalidInputException {
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            throw new InvalidInputException("Could not read from " + file, e);
        }
    }
}
