package rayengine.algorithms;

import static rayengine.test.MathUtil.RADIANTS_PER_DEGREE;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import rayengine.casters.Character;
import rayengine.test.databinding.RayModel;

public class EllipticTangentialRayAlgorithm implements IRayAlgorithm{

	private static final String NAME = "Elliptic Tangential";
	
	private boolean _showEllipsis = true;
	private boolean _fillPolygon = true;
	
	@Override
	public void castShadow(Canvas canvas, Character character, GC gc) {
		Rectangle clientArea = canvas.getClientArea();
		int azimuth = RayModel.getAzimuth();
		int altitude = RayModel.getAltitude();
		int pixelSize = RayModel.getPixelSize();
		double inclinationFactor = RayModel.getInclinationFactor();
		double relativeProjectionShiftX = Math.tan(altitude*RADIANTS_PER_DEGREE)*Math.sin(azimuth*RADIANTS_PER_DEGREE);
		double relativeProjectionShiftY = -Math.tan(altitude*RADIANTS_PER_DEGREE)*Math.cos(azimuth*RADIANTS_PER_DEGREE)*inclinationFactor;
		
		int leftCharacterBorder = clientArea.width/2 - character.getSheetWidth()*pixelSize/2;
		int bottomCharacterBorder = clientArea.height/2 + character.getSheetHeight()*pixelSize/2;
		int[][] frontBounds = character.getFrontBounds();
		int[][] rightBounds = character.getRightBounds();
		int bottomOffsetY = rightBounds[rightBounds.length-1][0];
		
		double squareTanOfAzimuth = square(Math.tan(RADIANTS_PER_DEGREE*azimuth));
		
		Point[] axis = new Point[character.getSheetHeight()];
		Point[][] tangentialPoints = new Point[character.getSheetHeight()][2];
		calculateAllAxisAndTangentialPoints(azimuth, squareTanOfAzimuth, pixelSize, frontBounds, rightBounds, axis,
				tangentialPoints);
		
		int[] polygonCoordinates = new int[character.getSheetHeight()*4];

		gc.setBackground(canvas.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		
		for(int row = character.getSheetHeight()-1; row >= 0;row--) {
			int axisX = axis[row].x;
			int axisY = axis[row].y;
			
			int projectedAxisY = (int) Math.round(axisY*inclinationFactor);
			
			int distanceFromBottom = character.getSheetHeight()-row-1;
			int leftProjectedEllipsisBorder = leftCharacterBorder + pixelSize*frontBounds[row][0] + (int) Math.round(distanceFromBottom*relativeProjectionShiftX*pixelSize);
			int topProjectedEllipsisBorder = bottomCharacterBorder + (bottomOffsetY - rightBounds[row][0])*pixelSize - projectedAxisY + (int) Math.round(distanceFromBottom*relativeProjectionShiftY*pixelSize);
			
			if(_showEllipsis) {
				gc.fillOval(leftProjectedEllipsisBorder, topProjectedEllipsisBorder, axisX, projectedAxisY);
			}
			polygonCoordinates[row*2] = leftProjectedEllipsisBorder + axisX/2 + tangentialPoints[row][0].x;
			polygonCoordinates[row*2+1] = topProjectedEllipsisBorder + (int) Math.round(projectedAxisY/2.0 - tangentialPoints[row][0].y*inclinationFactor);
			polygonCoordinates[polygonCoordinates.length - 2 - row*2] = leftProjectedEllipsisBorder + axisX/2 + tangentialPoints[row][1].x;
			polygonCoordinates[polygonCoordinates.length - 1 - row*2] = topProjectedEllipsisBorder + (int) Math.round(projectedAxisY/2.0 - tangentialPoints[row][1].y*inclinationFactor);
		}
		
		if(_fillPolygon) {
			gc.fillPolygon(polygonCoordinates);
		} else {
			gc.setForeground(canvas.getDisplay().getSystemColor(SWT.COLOR_RED));
			gc.drawPolygon(polygonCoordinates);
		}
	}

	private void calculateAllAxisAndTangentialPoints(int azimuth, double squareTanOfAzimuth, int pixelSize
			, int[][] frontBounds, int[][] rightBounds, Point[] axis, Point[][] tangentialPoints) {
		for(int row = 0; row < axis.length; row ++) {
			int axisX = (frontBounds[row][1] - frontBounds[row][0] + 1)*pixelSize;
			int axisY = (rightBounds[row][1] - rightBounds[row][0] + 1)*pixelSize;
			axis[row] = new Point(axisX, axisY);
			tangentialPoints[row] = calculateRayTangentialPoints(axisX, axisY, azimuth, squareTanOfAzimuth);
		}
	}
	
	
	
	private Point[] calculateRayTangentialPoints(int axisX, int axisY, int azimuth, double squareTanOfAzimuth) {
		double c = squareTanOfAzimuth*axisY/((double) axisX);
		int tangentialX = (int) Math.round(axisX / Math.sqrt(1 + c) /2.0);
		int tangentialY = (int) Math.round(axisY / Math.sqrt(1 + 1/c) /2.0);
		if(azimuth > 90 && azimuth < 270) {
			tangentialX = -tangentialX;
		}
		if(azimuth < 180) {
			tangentialY = -tangentialY;
		}
		return new Point[]{new Point(tangentialX, tangentialY), new Point(-tangentialX, -tangentialY)};
	}
	
	private double square(double x) {
		return x*x;
	}

	@Override
	public void buildOptionComposite(Composite parent) {
		Button ellipsisButton = new Button(parent, SWT.CHECK);
		ellipsisButton.setText("Render Ellipsis");
		ellipsisButton.setSelection(_showEllipsis);
		
		Button fillPolygonButton = new Button(parent, SWT.CHECK);
		fillPolygonButton.setText("Fill Polygon");
		fillPolygonButton.setSelection(_fillPolygon);
		
		ellipsisButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selection = ((Button) e.getSource()).getSelection();
				setShowEllipsis(selection);
				RayModel.genericUpdate();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}
		});
		
		fillPolygonButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selection = ((Button) e.getSource()).getSelection();
				setFillPolygon(selection);
				RayModel.genericUpdate();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}
		});
	}
	
	public void setShowEllipsis(boolean showEllipsis) {
		_showEllipsis = showEllipsis;
	}

	public void setFillPolygon(boolean fillPolygon) {
		_fillPolygon = fillPolygon;
	}

	@Override
	public String getName() {
		return NAME;
	}
}
