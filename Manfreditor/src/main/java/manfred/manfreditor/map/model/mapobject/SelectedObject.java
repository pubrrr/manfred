package manfred.manfreditor.map.model.mapobject;

import java.util.Optional;

public class SelectedObject {

    private SelectionState selectionState;

    public SelectedObject(SelectionState initialSelection) {
        this.selectionState = initialSelection;
    }

    public void select(MapObjectRepository.ObjectKey objectKey) {
        this.selectionState = selectionState.select(objectKey);
    }

    public Optional<MapObjectRepository.ObjectKey> getSelection() {
        return selectionState.getSelection();
    }

    public boolean isSelected(MapObjectRepository.ObjectKey objectKey) {
        return selectionState.getSelection()
            .map(selectedKey -> selectedKey.equals(objectKey))
            .orElse(false);
    }
}
