package manfred.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import manfred.game.GameConfig;
import manfred.game.graphics.ImageLoader;
import manfred.infrastructure.person.GelaberConverter;
import manfred.infrastructure.person.PersonDtoReader;
import manfred.infrastructure.person.PersonReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class InfrastructureContext {

    @Bean
    public PersonReader personReader(GelaberConverter gelaberConverter, GameConfig gameConfig, ImageLoader imageLoader, PersonDtoReader personDtoReader) {
        return new PersonReader(gelaberConverter, gameConfig, imageLoader, personDtoReader);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper(new YAMLFactory());
    }
}
