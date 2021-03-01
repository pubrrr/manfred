package manfred.manfreditor.mapobject;

import java.util.Optional;

public class KeySelection implements SelectionState {

    private final MapObjectRepository.ObjectKey selectedKey;

    public KeySelection(MapObjectRepository.ObjectKey selectedKey) {
        this.selectedKey = selectedKey;
    }

    @Override
    public SelectionState select(MapObjectRepository.ObjectKey newSelectedKey) {
        if (this.selectedKey.equals(newSelectedKey)) {
            return SelectionState.empty();
        }
        return new KeySelection(newSelectedKey);
    }

    @Override
    public Optional<MapObjectRepository.ObjectKey> getSelection() {
        return Optional.of(selectedKey);
    }
}
