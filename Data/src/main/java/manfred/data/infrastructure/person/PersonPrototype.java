package manfred.data.infrastructure.person;

import lombok.Value;
import manfred.data.infrastructure.person.gelaber.GelaberPrototype;

import java.awt.image.BufferedImage;

@Value
public class PersonPrototype {
    String name;
    GelaberPrototype gelaber;
    BufferedImage image;
    int positionX;
    int positionY;
}
