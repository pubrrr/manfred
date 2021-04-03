package manfred.manfreditor.map.model.mapobject;

import java.util.Optional;

public class EmptySelection implements SelectionState {

    @Override
    public SelectionState select(MapObjectRepository.ObjectKey selectedKey) {
        return new KeySelection(selectedKey);
    }

    @Override
    public Optional<MapObjectRepository.ObjectKey> getSelection() {
        return Optional.empty();
    }
}
