package rayengine.algorithms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;

import rayengine.casters.AbstractCaster;
import rayengine.casters.AbstractSection;
import rayengine.dummy.databinding.RayModel;

public class SimpleSectionAlgorithm implements IShadowAlgorithm{

	private static final String NAME = "Sections";
	
	@Override
	public void castShadow(Canvas canvas, AbstractCaster<? extends AbstractSection> caster, GC gc) {
		gc.setBackground(canvas.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		int azimuth = RayModel.getAzimuth();
		int altitude = RayModel.getAltitude();
		caster.castSections(altitude, azimuth);
		caster.drawFillSections(gc);
	}

	@Override
	public String getName() {
		return NAME;
	}
}
