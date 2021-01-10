package manfred.data.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
