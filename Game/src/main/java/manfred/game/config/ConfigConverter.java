package manfred.game.config;

import manfred.data.config.ConfigDto;
import org.springframework.stereotype.Component;

@Component
public class ConfigConverter {

    public GameConfig convert(ConfigDto configDto) {
        return new GameConfig(
            configDto.getWindowSize().getWidth(),
            configDto.getWindowSize().getHeight(),
            configDto.getPixelBlockSize(),
            configDto.getTextBoxDistanceToBorder(),
            configDto.getTextPointSize(),
            configDto.getTextDistanceToBox(),
            configDto.getGelaberBoxPosition().getBoxPositionX(),
            configDto.getGelaberBoxPosition().getBoxPositionY()
        );
    }
}
