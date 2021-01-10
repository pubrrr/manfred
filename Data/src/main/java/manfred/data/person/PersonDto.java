package manfred.data.person;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;

@Data
@NoArgsConstructor
public class PersonDto {
    private String name;
    private GelaberDto gelaber;
    private BufferedImage image;
}
