package componentTests;

import helpers.TestGameConfig;
import manfred.data.persistence.reader.AttackReader;
import manfred.data.persistence.reader.ConfigProvider;
import manfred.data.persistence.reader.ImageLoader;
import manfred.game.GameContext;
import manfred.infrastructureadapter.attack.AttackGeneratorConverter;
import manfred.infrastructureadapter.config.ConfigConverter;
import manfred.game.config.GameConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan
public class TestGameContext extends GameContext {
    @Override
    public GameConfig gameConfig(ConfigProvider configReader, ConfigConverter configConverter) {
        return new TestGameConfig();
    }
}
