package manfred.data.persistence.reader;

import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.Optional;

@Component
public class UrlHelper {

    public File getFileForMap(String name) {
        return new File(getClass().getResource("/maps").getFile(), name + ".yaml");
    }

    public File getFileForPerson(String name) {
        return new File(getClass().getResource("/persons").getFile(), name + ".yaml");
    }

    public Optional<URL> getImageResourceForPerson(String name) {
        return Optional.ofNullable(getClass().getResource("/persons/" + name + ".png"));
    }

    public File getFileForEnemy(Object name) {
        return new File(getClass().getResource("/enemies").getFile(), name + ".yaml");
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
