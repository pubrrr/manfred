package rayengine.algorithms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import rayengine.casters.Character;

public interface IRayAlgorithm {
	void castShadow(Canvas canvas, Character character, GC gc);
	default void buildOptionComposite(Composite parent) {
		new Label(parent, SWT.NONE).setText("No options available");
	};
	String getName();
}
