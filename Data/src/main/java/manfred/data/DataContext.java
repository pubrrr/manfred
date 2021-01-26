package manfred.data;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import manfred.data.infrastructure.map.validator.EnemyValidator;
import manfred.data.persistence.reader.UrlHelper;
import manfred.data.infrastructure.map.validator.DoorsValidator;
import manfred.data.infrastructure.map.validator.NoTwoObjectsAtSameTileValidator;
import manfred.data.infrastructure.map.validator.PersonsValidator;
import manfred.data.infrastructure.map.validator.PortalsValidator;
import manfred.data.infrastructure.map.validator.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ComponentScan(basePackages = "manfred.data")
public class DataContext {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper(new YAMLFactory());
    }

    @Bean(name = "mapValidators")
    public List<Validator> mapValidators(UrlHelper urlHelper) {
        return List.of(
            new PersonsValidator(urlHelper),
            new EnemyValidator(urlHelper),
            new PortalsValidator(urlHelper),
            new DoorsValidator(urlHelper),
            new NoTwoObjectsAtSameTileValidator()
        );
    }
}
