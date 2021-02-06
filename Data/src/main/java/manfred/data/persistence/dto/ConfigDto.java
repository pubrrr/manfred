package manfred.data.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import manfred.data.shared.PositiveInt;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigDto {
    private WindowSizeDto windowSize;
    private GelaberBoxPositionDto gelaberBoxPosition;
    private PositiveInt.Strict pixelBlockSize;
    private PositiveInt textBoxDistanceToBorder;
    private PositiveInt textPointSize;
    private PositiveInt textDistanceToBox;
}
