package manfred.manfreditor.mapobject;

import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.TilePrototype;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

public class ConcreteMapObject implements MapObject {

    private final String name;
    private final MapMatrix<TilePrototype> structure; // TODO probably change type of this
    private final ImageData imageData;

    public ConcreteMapObject(String name, MapMatrix<TilePrototype> structure, ImageData imageData) {
        this.name = name;
        this.structure = structure;
        this.imageData = imageData;
    }

    @Override
    public void draw(GC gc, Display display) {
        Image image = new Image(display, this.imageData);
        gc.drawImage(image, 50, 50);
    }

    public String getName() {
        return this.name;
    }
}
