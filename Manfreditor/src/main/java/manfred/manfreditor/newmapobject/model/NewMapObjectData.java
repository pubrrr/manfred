package manfred.manfreditor.newmapobject.model;

import lombok.Value;
import org.eclipse.swt.graphics.ImageData;

@Value
public class NewMapObjectData {
    String name;
    ImageData imageData;
    AccessibilityGrid accessibilityGrid;
}
