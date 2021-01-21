package manfred.data.person;

import lombok.Value;
import manfred.data.person.gelaber.ValidatedGelaberDto;

import java.awt.image.BufferedImage;

@Value
public class LocatedPersonDtoBuilder {
    String name;
    ValidatedGelaberDto validatedGelaberDto;
    BufferedImage image;

    public LocatedPersonDto at(int positionX, int positionY) {
        return new LocatedPersonDto(
            this.name,
            this.validatedGelaberDto,
            this.image,
            positionX,
            positionY
        );
    }
}
