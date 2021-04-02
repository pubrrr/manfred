package manfred.data.persistence.reader;

import org.springframework.stereotype.Component;

import java.io.File;
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

    public Optional<URL> getResourceForEnemy(Object name) {
        return Optional.ofNullable(getClass().getResource("/enemies/" + name + ".yaml"));
    }

    public Optional<URL> getImageResourceForEnemy(String name) {
        return Optional.ofNullable(getClass().getResource("/enemies/" + name + ".png"));
    }

    public Optional<URL> getManfredFrame(String name) {
        return Optional.ofNullable(getClass().getResource("/manfred/" + name + ".png"));
    }

    public File getFileForMapTile(String name) {
        return new File(getClass().getResource("/maps/tiles").getFile(), name + ".yaml");
    }

    public File getImageFileForMapTile(String name) {
        return new File(getClass().getResource("/maps/tiles").getFile(), name + ".png");
    }
}
