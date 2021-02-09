package componentTests;

import helpers.TestGameConfig;
import manfred.data.InvalidInputException;
import manfred.data.persistence.reader.ConfigProvider;
import manfred.game.GameContext;
import manfred.game.config.GameConfig;
import manfred.infrastructureadapter.config.ConfigConverter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class TestGameContext extends GameContext {
    @Override
    public GameConfig gameConfig(ConfigProvider configReader, ConfigConverter configConverter) throws InvalidInputException {
        return new TestGameConfig();
    }
}
