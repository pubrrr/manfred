package rayengine.casters;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import rayengine.dummy.MathUtil;
import rayengine.dummy.databinding.RayModel;

public class Character extends AbstractCaster<Ellipsis>{
	
	public Character(String relavitePath, Display display) {
		super(relavitePath, display);
	}

	@Override
	protected void initializeSections(int height) {
		sections = new Ellipsis[height];
	}

	@Override
	protected Ellipsis createSection(int width, int height) {
		return new Ellipsis(width, height);
	}
	
	@Override
	public void castShadowOnSelf(GC gc) {
		List<List<Boolean>> darkSpots = new ArrayList<>(height-1);
		for(int rowFromTop = 1; rowFromTop < height; rowFromTop++) {
			Ellipsis currentSection = sections[rowFromTop];
			List<Boolean> darkSpotsInRow = new ArrayList<>(leftToRightBounds[rowFromTop].getWidth());
			float prefactor = currentSection.height / (float) currentSection.width;
			int aSquare = currentSection.width*currentSection.width/4;
			for(int spot = 0; spot < leftToRightBounds[rowFromTop].getWidth(); spot++) {
				int relativeX = (spot-leftToRightBounds[rowFromTop].getWidth()/2) * RayModel.getPixelSize();
				int relativeY = (int) Math.round((prefactor * Math.sqrt((double) (aSquare - relativeX*relativeX))));
				Point pointOnEllipsis = MathUtil.add(currentSection.getCenter(), new Point(relativeX, relativeY));
				darkSpotsInRow.add(false);
				for(int rowAbove = 0; rowAbove < rowFromTop; rowAbove++) {
					if(sections[rowAbove].contains(pointOnEllipsis)) {
						darkSpotsInRow.set(spot, true);
						break;
					}
				}
			}
			darkSpots.add(darkSpotsInRow);
		}
		
		drawShadowOnSelf(darkSpots, gc);
	}

	private void drawShadowOnSelf(List<List<Boolean>> darkSpots, GC gc) {
		for(int rowFromTop = 0; rowFromTop < height-1; rowFromTop++) {
			List<Boolean> darkSpotsInRow = darkSpots.get(rowFromTop);
			int absoluteY = getBottomBorderCoordinate() + (rowFromTop+1 - height)*RayModel.getPixelSize();
			for(int spot = 0; spot < leftToRightBounds[rowFromTop+1].getWidth(); spot++) {
				if(darkSpotsInRow.get(spot)) {
					int absoluteX = getLeftBorderCoordinate() + (leftToRightBounds[rowFromTop+1].getLowerBound() + spot)*RayModel.getPixelSize();
					gc.fillRectangle(absoluteX, absoluteY, RayModel.getPixelSize(), RayModel.getPixelSize());
				}
			}
		}
	}
}
