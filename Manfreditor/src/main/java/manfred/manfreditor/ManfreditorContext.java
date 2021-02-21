package manfred.manfreditor;

import manfred.data.infrastructure.map.TileConversionRule;
import manfred.manfreditor.map.Map;
import manfred.manfreditor.map.MapModel;
import manfred.manfreditor.mapobject.MapObject;
import manfred.manfreditor.map.objectfactory.ConcreteMapObjectFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
@ComponentScan(basePackages = "manfred.manfreditor")
public class ManfreditorContext {

    public MapModel mapModel() {
        return new MapModel(new Map("uninitialized", new HashMap<>()));
    }

    public TileConversionRule<MapObject> tileConversionRule(ConcreteMapObjectFactory concreteMapObjectFactory) {
        return concreteMapObjectFactory;
    }
}
