package componentTests;

import helpers.TestGameConfig;
import manfred.data.config.ConfigProvider;
import manfred.data.image.ImageLoader;
import manfred.game.GameContext;
import manfred.game.attack.AttackReader;
import manfred.game.config.ConfigConverter;
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

    @Bean
    public ImageLoader imageLoader() {
        return mock(ImageLoader.class);
    }

    @Bean
    public AttackReader attackReader() {
        return mock(AttackReader.class);
    }
}
