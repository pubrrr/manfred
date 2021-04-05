package manfred.manfreditor.map.model;

import io.vavr.collection.List;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

@Component
public class MapRepository {

    public MapKey populateWith(Map map) {
        return null;
    }

    public Map get(MapKey key) {
        return null;
    }

    public List<MapKey> getKeys() {
        return List.empty();
    }

    @EqualsAndHashCode
    public static class MapKey {

        public final String value;

        private MapKey(String value) {
            this.value = value;
        }
    }
}
