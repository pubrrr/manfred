package rayengine.algorithms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import rayengine.casters.AbstractCaster;
import rayengine.casters.AbstractSection;
import rayengine.casters.Character;

public interface IShadowAlgorithm {
	void castShadow(Canvas canvas, AbstractCaster<? extends AbstractSection> caster, GC gc);
	default void buildOptionComposite(Composite parent) {
		new Label(parent, SWT.NONE).setText("No options available");
	};
	String getName();
}
