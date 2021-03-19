package rayengine.casters;

import org.eclipse.swt.graphics.GC;

public interface ISectionDrawConsumer {
	void drawSection(GC gc, int leftBorder, int bottomBorder, int width, int height);
}
