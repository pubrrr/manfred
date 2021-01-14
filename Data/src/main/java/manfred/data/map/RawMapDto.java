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
    private List<String> map;
    private List<PersonDto> persons;
    private List<TransporterDto> portals;
    private List<TransporterDto> doors;
    private List<EnemyDto> enemies;
}
