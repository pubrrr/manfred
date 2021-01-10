package manfred.data.person;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class GelaberDto {
    private String initialTextReference;
    private Map<String, GelaberTextDto> texts;
}
