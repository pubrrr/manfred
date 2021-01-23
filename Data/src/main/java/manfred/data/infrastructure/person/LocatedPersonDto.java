package manfred.data.infrastructure.person;

import lombok.Value;
import manfred.data.infrastructure.person.gelaber.ValidatedGelaberDto;

import java.awt.image.BufferedImage;

@Value
public class LocatedPersonDto {
    String name;
    ValidatedGelaberDto gelaber;
    BufferedImage image;
    int positionX;
    int positionY;
}
