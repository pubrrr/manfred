package manfred.data.persistence.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import manfred.data.persistence.reader.MapSource;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawMapDto {
    private String name;
    private List<String> map = List.of();
    private List<MapPersonDto> persons = List.of();
    private List<TransporterDto> portals = List.of();
    private List<TransporterDto> doors = List.of();
    private List<MapEnemyDto> enemies = List.of();
    @JsonIgnore
    private MapSource mapSource;
}
