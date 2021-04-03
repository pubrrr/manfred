package manfred.manfreditor.map.model.mapobject;

import java.util.Optional;

public interface SelectionState {

    SelectionState select(MapObjectRepository.ObjectKey selectedKey);

    Optional<MapObjectRepository.ObjectKey> getSelection();

    static SelectionState empty() {
        return new EmptySelection();
    }
}
