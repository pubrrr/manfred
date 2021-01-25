package rayengine.algorithms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;

import rayengine.casters.Character;
import rayengine.casters.Ellipsis;
import rayengine.test.databinding.RayModel;

public class EllipticRayAlgorithm implements IRayAlgorithm{

	private static final String NAME = "Elliptic";
	
	@Override
	public void castShadow(Canvas canvas, Character character, GC gc) {
		gc.setBackground(canvas.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		int azimuth = RayModel.getAzimuth();
		int altitude = RayModel.getAltitude();
		character.castSections(altitude, azimuth);
		int leftBoundCoordinate = character.getLeftBoundCoordinate();
		int bottomBoundCoordinate = character.getBottomBorderCoordinate();
		Ellipsis[] sections = character.getSections();
		for(int row = 0; row < sections.length; row ++) {
			Ellipsis section = sections[row];
			gc.fillOval(leftBoundCoordinate + section.getLeftBound(), bottomBoundCoordinate + section.getBottomBound() - section.getAxisY(), section.getAxisX(), section.getAxisY());
		}
	}

	@Override
	public String getName() {
		return NAME;
	}

}
