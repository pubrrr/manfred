package manfred.data.person;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import manfred.data.person.gelaber.GelaberDto;

import java.awt.image.BufferedImage;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {
    private String name;
    private GelaberDto gelaber;
    @JsonIgnore
    private BufferedImage image;
}
