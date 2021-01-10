package componentTests;

import helpers.TestGameConfig;
import manfred.game.ConfigReader;
import manfred.game.GameConfig;
import manfred.game.GameContext;
import manfred.game.attack.AttackReader;
import manfred.game.graphics.ImageLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan
public class TestGameContext extends GameContext {
    @Override
    public GameConfig gameConfig(ConfigReader configReader) {
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
