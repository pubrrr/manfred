package manfred.data.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import manfred.data.shared.PositiveInt;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WindowSizeDto {
    private PositiveInt width;
    private PositiveInt height;
}
