package manfred.data.infrastructure.person;

import lombok.Value;
import manfred.data.infrastructure.person.gelaber.GelaberPrototype;
import manfred.data.shared.PositiveInt;

import java.awt.image.BufferedImage;

@Value
public class PersonPrototypeBuilder {
    String name;
    GelaberPrototype gelaberPrototype;
    BufferedImage image;

    public PersonPrototype at(PositiveInt positionX, PositiveInt positionY) {
        return new PersonPrototype(
            this.name,
            this.gelaberPrototype,
            this.image,
            positionX,
            positionY
        );
    }
}
