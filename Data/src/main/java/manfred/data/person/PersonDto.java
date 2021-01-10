package manfred.data.person;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;

@Data
@NoArgsConstructor
public class PersonDto {
    private String name;
    private GelaberDto gelaber;
    @JsonIgnore private BufferedImage image;
}
