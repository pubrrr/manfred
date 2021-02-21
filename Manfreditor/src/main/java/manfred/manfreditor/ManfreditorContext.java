package manfred.manfreditor;

import manfred.data.DataContext;
import manfred.data.infrastructure.map.TileConversionRule;
import manfred.manfreditor.map.Map;
import manfred.manfreditor.map.MapModel;
import manfred.manfreditor.mapobject.MapObject;
import manfred.manfreditor.map.objectfactory.ConcreteMapObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.HashMap;

@Configuration
@ComponentScan(basePackages = "manfred.manfreditor")
@Import({DataContext.class})
public class ManfreditorContext {

    @Bean
    public MapModel mapModel() {
        return new MapModel(new Map("uninitialized", new HashMap<>()));
    }

    @Bean
    public TileConversionRule<MapObject> tileConversionRule(ConcreteMapObjectFactory concreteMapObjectFactory) {
        return concreteMapObjectFactory;
    }
}
