package manfred.data.person;

import lombok.Value;
import manfred.data.person.gelaber.ValidatedGelaberDto;

import java.awt.image.BufferedImage;

@Value
public class LocatedPersonDto {
    String name;
    ValidatedGelaberDto gelaber;
    BufferedImage image;
    int positionX;
    int positionY;
}
