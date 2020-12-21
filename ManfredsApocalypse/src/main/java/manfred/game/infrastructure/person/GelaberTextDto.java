package manfred.game.infrastructure.person;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GelaberTextDto {
    private String text;
    private List<ReferenceDto> references;
}
