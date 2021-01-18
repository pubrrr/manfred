package manfred.data.map;

import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Optional;

@Component
public class MapHelper {

    public Optional<URL> getResourceForMap(String name) {
        return Optional.ofNullable(getClass().getResource("/maps/" + name + ".yaml"));
    }
}
