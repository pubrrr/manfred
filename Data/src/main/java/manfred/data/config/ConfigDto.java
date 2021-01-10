package manfred.data.config;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConfigDto {
    private WindowSizeDto windowSize;
    private GelaberBoxPositionDto gelaberBoxPosition;
    private int pixelBlockSize;
    private int textBoxDistanceToBorder;
    private int textPointSize;
    private int textDistanceToBox;
}
