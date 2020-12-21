package manfred.game.infrastructure.person;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonDto {
    private String name;
    private GelaberDto gelaber;
}
