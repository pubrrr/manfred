package manfred.data;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import manfred.data.enemy.EnemyReader;
import manfred.data.map.MapDtoValidator;
import manfred.data.map.MapHelper;
import manfred.data.map.tile.TileConverter;
import manfred.data.map.validator.DoorsValidator;
import manfred.data.map.validator.NoTwoObjectsAtSameTileValidator;
import manfred.data.map.validator.PersonsValidator;
import manfred.data.map.validator.PortalsValidator;
import manfred.data.map.validator.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ComponentScan(basePackages = "manfred.data")
public class DataContext {
    public static final String PATH_DATA = "Data\\data\\";

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper(new YAMLFactory());
    }

    @Bean(name = "mapValidators")
    public List<Validator> mapValidators(MapHelper mapHelper) {
        return List.of(
            new PersonsValidator(),
            new PortalsValidator(mapHelper),
            new DoorsValidator(mapHelper),
            new NoTwoObjectsAtSameTileValidator()
        );
    }
}
