package manfred.data.map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawMapDto {
    private String name;
    private List<String> map = List.of();
    private List<PersonDto> persons = List.of();
    private List<TransporterDto> portals = List.of();
    private List<TransporterDto> doors = List.of();
    private List<EnemyDto> enemies = List.of();
}
