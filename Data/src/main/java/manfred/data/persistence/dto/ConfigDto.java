package manfred.data.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import manfred.data.persistence.dto.GelaberBoxPositionDto;
import manfred.data.persistence.dto.WindowSizeDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigDto {
    private WindowSizeDto windowSize;
    private GelaberBoxPositionDto gelaberBoxPosition;
    private int pixelBlockSize;
    private int textBoxDistanceToBorder;
    private int textPointSize;
    private int textDistanceToBox;
}
