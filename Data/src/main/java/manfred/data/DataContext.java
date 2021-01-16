package manfred.data;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import manfred.data.map.MapDtoValidator;
import manfred.data.map.tile.TileConverter;
import manfred.data.map.validator.PersonsValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ComponentScan(basePackages = "manfred.data")
public class DataContext {
    public static final String PATH_DATA = "ManfredsApocalypse\\Data\\data\\";

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper(new YAMLFactory());
    }

    @Bean
    public MapDtoValidator mapDtoValidator(TileConverter tileConverter) {
        return new MapDtoValidator(List.of(new PersonsValidator()), tileConverter);
    }
}
