package rayengine.algorithms;

import static rayengine.test.MathUtil.RADIANTS_PER_DEGREE;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;

import rayengine.casters.CasterBounds;
import rayengine.casters.Character;
import rayengine.test.databinding.RayModel;

public class SlidingEllipsesRayAlgorithm implements IRayAlgorithm{

	@Override
	public void castShadow(Canvas canvas, Character character, GC gc) {
		Rectangle clientArea = canvas.getClientArea();
		gc.setBackground(canvas.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		int azimuth = RayModel.getAzimuth();
		int altitude = RayModel.getAltitude();
		int pixelSize = RayModel.getPixelSize();
		double inclinationFactor = RayModel.getInclinationFactor();
		double relativeProjectionShiftX = Math.tan(altitude*RADIANTS_PER_DEGREE)*Math.sin(azimuth*RADIANTS_PER_DEGREE);
		double relativeProjectionShiftY = -Math.tan(altitude*RADIANTS_PER_DEGREE)*Math.cos(azimuth*RADIANTS_PER_DEGREE)*inclinationFactor;
		
		int leftCharacterBorder = clientArea.width/2 - character.getSheetWidth()*pixelSize/2;
		int bottomCharacterBorder = clientArea.height/2 + character.getSheetHeight()*pixelSize/2;
		CasterBounds[] frontBounds = character.getFrontBounds();
		CasterBounds[] rightBounds = character.getRightBounds();
		int bottomOffsetY = rightBounds[rightBounds.length-1].getLowerBound();
		
		for(int row = character.getSheetHeight()-1; row >=0;row--) {
			int axisX1 = frontBounds[row].getWidth()*pixelSize;
			int axisY1 = (int) (rightBounds[row].getWidth()*pixelSize*inclinationFactor);
			int distanceFromBottom1 = character.getSheetHeight()-row-1;
			int centerX1 = leftCharacterBorder + pixelSize*frontBounds[row].getLowerBound() + (int) Math.round(distanceFromBottom1*relativeProjectionShiftX*pixelSize) + axisX1/2;
			int centerY1 = bottomCharacterBorder + (bottomOffsetY - rightBounds[row].getLowerBound())*pixelSize - axisY1/2 + (int) Math.round(distanceFromBottom1*relativeProjectionShiftY*pixelSize);
			if(row == 0) {
				gc.fillOval(centerX1 - axisX1/2, centerY1 - axisY1/2, axisX1, axisY1);
			} else {
				int axisX2 = frontBounds[row-1].getWidth()*pixelSize;
				int axisY2 = (int) Math.round((rightBounds[row-1].getWidth())*pixelSize*inclinationFactor);

				int distanceFromBottom2 = character.getSheetHeight()-row;
				int centerX2 = leftCharacterBorder + pixelSize*frontBounds[row-1].getLowerBound() + (int) Math.round(distanceFromBottom2*relativeProjectionShiftX*pixelSize) + axisX2/2;
				int centerY2 = bottomCharacterBorder + (bottomOffsetY - rightBounds[row-1].getLowerBound())*pixelSize - axisY2/2 + (int) Math.round(distanceFromBottom2*relativeProjectionShiftY*pixelSize);
				
				int slidingDistance;
				if(Math.abs(centerX2 - centerX1) > Math.abs(centerY2 - centerY1)) {
					slidingDistance = Math.abs(centerX2 - centerX1);
				} else {
					slidingDistance = Math.abs(centerY2 - centerY1);
				}
				for(int slider = 0; slider < slidingDistance; slider++) {
					double slidingFactor = slider / ((double) slidingDistance);
					int axisXslide = (int) Math.round(axisX1*(1-slidingFactor)) + (int) Math.round(axisX2*slidingFactor);
					int axisYslide = (int) Math.round(axisY1*(1-slidingFactor)) + (int) Math.round(axisY2*slidingFactor);
					int centerXslide = (int) Math.round(centerX1*(1-slidingFactor)) + (int) Math.round(centerX2*slidingFactor);
					int centerYslide = (int) Math.round(centerY1*(1-slidingFactor)) + (int) Math.round(centerY2*slidingFactor);

					gc.fillOval(centerXslide - axisXslide/2, centerYslide - axisYslide/2, axisXslide, axisYslide);
				}
			}
		}
	}
	
	@Override
	public String getName() {
		return "Sliding Ellipses";
	}

}
