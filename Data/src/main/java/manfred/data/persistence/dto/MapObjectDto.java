package manfred.data.persistence.dto;

import manfred.data.infrastructure.map.MapObject;

public interface MapObjectDto extends MapObject {

    String getTargetToLoad();
}
