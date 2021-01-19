package manfred.data.helper;

import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Optional;

@Component
public class UrlHelper {

    public Optional<URL> getResourceForMap(String name) {
        return Optional.ofNullable(getClass().getResource("/maps/" + name + ".yaml"));
    }

    public Optional<URL> getResourceForPerson(String name) {
        return Optional.ofNullable(getClass().getResource("/persons/" + name + ".yaml"));
    }

    public Optional<URL> getImageResourceForPerson(String name) {
        return Optional.ofNullable(getClass().getResource("/persons/" + name + ".png"));
    }
}
