package manfred.manfreditor;

import manfred.manfreditor.map.Map;
import manfred.manfreditor.map.MapModel;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
@ComponentScan(basePackages = "manfred.manfreditor")
public class ManfreditorContext {

    public MapModel mapModel() {
        return new MapModel(new Map("uninitialized", new HashMap<>()));
    }
}
