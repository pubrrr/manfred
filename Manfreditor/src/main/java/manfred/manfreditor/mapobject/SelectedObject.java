package manfred.manfreditor.mapobject;

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
}
