package manfred.manfreditor;

import manfred.game.graphics.ImageLoader;
import manfred.manfreditor.map.MapReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManfreditorContext {
    @Bean
    public MapReader mapReader(ImageLoader imageLoader) {
        return new MapReader(imageLoader);
    }

    @Bean
    public ImageLoader imageLoader() {
        return new ImageLoader();
    }
}
