package manfred.data.persistence.reader;

import lombok.AllArgsConstructor;
import manfred.data.InvalidInputException;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.net.URL;

@Component
@AllArgsConstructor
public class ManfredFrameLoader {

    private final UrlHelper urlHelper;
    private final ImageLoader imageLoader;

    public BufferedImage loadFrame(String frameName) throws InvalidInputException {
        URL frameUrl = urlHelper.getManfredFrame(frameName)
            .orElseThrow(() -> new InvalidInputException("Did not find resource for Manfred frame " + frameName));

        return imageLoader.load(frameUrl);
    }

}
