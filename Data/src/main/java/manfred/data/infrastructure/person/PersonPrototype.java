package manfred.data.infrastructure.person;

import lombok.Value;
import manfred.data.infrastructure.map.MapObject;
import manfred.data.infrastructure.person.gelaber.GelaberPrototype;
import manfred.data.shared.PositiveInt;

import java.awt.image.BufferedImage;

@Value
public class PersonPrototype implements MapObject {
    String name;
    GelaberPrototype gelaber;
    BufferedImage image;
    PositiveInt positionX;
    PositiveInt positionY;
}
