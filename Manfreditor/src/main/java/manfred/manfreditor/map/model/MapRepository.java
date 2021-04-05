package manfred.manfreditor.map.model;

import io.vavr.collection.HashMap;
import io.vavr.collection.Set;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

@Component
public class MapRepository {

    private HashMap<MapKey, Map> repository = HashMap.empty();

    public MapKey populateWith(Map map) {
        MapKey newKey = new MapKey(map.getName());
        this.repository = this.repository.put(newKey, map);
        return newKey;
    }

    public Map get(MapKey key) {
        return this.repository.get(key).get();
    }

    public Set<MapKey> getKeys() {
        return this.repository.keySet();
    }

    @EqualsAndHashCode
    public static class MapKey {

        public final String value;

        private MapKey(String value) {
            this.value = value;
        }
    }
}
