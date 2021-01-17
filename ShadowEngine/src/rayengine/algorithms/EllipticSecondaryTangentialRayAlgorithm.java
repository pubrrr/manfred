package rayengine.algorithms;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import rayengine.casters.Character;
import rayengine.casters.Ellipsis;
import rayengine.test.MathUtil;
import rayengine.test.Pair;
import rayengine.test.databinding.RayModel;

public class EllipticSecondaryTangentialRayAlgorithm implements IRayAlgorithm{

	private static final String NAME = "Elliptic Secondary Tangential";
	
	private boolean _showEllipsis = true;
	private boolean _fillPolygon = true;
	private boolean _fillEllipsis = true;
	
	@Override
	public void castShadow(Canvas canvas, Character character, GC gc) {
		gc.setForeground(canvas.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		gc.setBackground(canvas.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		int azimuth = RayModel.getAzimuth();
		int altitude = RayModel.getAltitude();
		character.castSections(altitude, azimuth);
		int leftBoundCoordinate = character.getLeftBoundCoordinate();
		int bottomBoundCoordinate = character.getBottomBoundCoordinate();
		Ellipsis[] sections = character.getSections();
		if(_showEllipsis) {
			for(int row = 0; row < sections.length; row ++) {
				Ellipsis section = sections[row];
				if(_fillEllipsis) {
					gc.fillOval(leftBoundCoordinate + section.getLeftBound(), bottomBoundCoordinate + section.getBottomBound() - section.getAxisY(), section.getAxisX(), section.getAxisY());
				} else {
					gc.drawOval(leftBoundCoordinate + section.getLeftBound(), bottomBoundCoordinate + section.getBottomBound() - section.getAxisY(), section.getAxisX(), section.getAxisY());
				}
			}
		}
		
		Map<Integer, Point[]> polygons = calculateSecondaryTangentialPolygonPoints(sections);
		
		if(_fillPolygon) {
			for(Point[] polygon : polygons.values()) {
				gc.fillPolygon(MathUtil.extractCoordinates(polygon, new Point(character.getLeftBoundCoordinate(), character.getBottomBoundCoordinate())));
			}
		} else {
			gc.setForeground(canvas.getDisplay().getSystemColor(SWT.COLOR_RED));
			for(Point[] polygon : polygons.values()) {
				gc.drawLine(polygon[0].x+character.getLeftBoundCoordinate(), polygon[0].y+character.getBottomBoundCoordinate(), polygon[1].x+character.getLeftBoundCoordinate(), polygon[1].y+character.getBottomBoundCoordinate());
				gc.drawLine(polygon[2].x+character.getLeftBoundCoordinate(), polygon[2].y+character.getBottomBoundCoordinate(), polygon[3].x+character.getLeftBoundCoordinate(), polygon[3].y+character.getBottomBoundCoordinate());
			}
		}
	}
	
	private Map<Integer, Point[]> calculateSecondaryTangentialPolygonPoints(Ellipsis[] sections){
		Map<Integer, Point[]> rowToPolygons = new HashMap<>();
		for(int row=0; row < sections.length-1; row++) {
//		int row = 3;
			Ellipsis section = sections[row];
			Ellipsis sectionBelow = sections[row+1];
			if(!Ellipsis.oneContainsTheOther(section, sectionBelow)) {
				Point relativeCenter = Ellipsis.calculateRelativeCenter(section, sectionBelow);
				Point primaryTangentialPoint1 = calculateRelativeTangentialPoint(section, relativeCenter);
				Point primaryTangentialPointBelow1 = calculateRelativeTangentialPoint(sectionBelow, relativeCenter);
				Pair<Point> secondaryTangent1 = calculateAbsoluteSecondOrderTangentialPoints(section, sectionBelow, relativeCenter, primaryTangentialPoint1,
						primaryTangentialPointBelow1);
				Point primaryTangentialPoint2 = new Point(-primaryTangentialPoint1.x, -primaryTangentialPoint1.y);
				Point primaryTangentialPointBelow2 = new Point(-primaryTangentialPointBelow1.x, -primaryTangentialPointBelow1.y);
				relativeCenter.x *= -1;
				relativeCenter.y *= -1;
				Pair<Point> secondaryTangent2 = calculateAbsoluteSecondOrderTangentialPoints(sectionBelow, section, relativeCenter, primaryTangentialPointBelow2,
						primaryTangentialPoint2);
				rowToPolygons.put(row, new Point[] {secondaryTangent1.getValue1(), secondaryTangent1.getValue2(), secondaryTangent2.getValue1(), secondaryTangent2.getValue2()});
			}
		}
		return rowToPolygons;
	}

	private Pair<Point> calculateAbsoluteSecondOrderTangentialPoints(Ellipsis section, Ellipsis sectionBelow, Point relativeCenter,
			Point primaryTangentialPoint, Point primaryTangentialPointBelow) {
		Point primaryTangent = MathUtil.add(relativeCenter, MathUtil.calculateRelativePosition(primaryTangentialPoint, primaryTangentialPointBelow));
		Point secondaryTangentialPoint = calculateRelativeTangentialPoint(section, primaryTangent);
		Point secondaryTangentialPointBelow = calculateRelativeTangentialPoint(sectionBelow, primaryTangent);
		return new Pair<Point>(MathUtil.add(secondaryTangentialPoint, section.getCenter()), MathUtil.add(secondaryTangentialPointBelow, sectionBelow.getCenter()));
	}
	
	private Point calculateRelativeTangentialPoint(Ellipsis ellipsis, Point tangentDirection) {
		int axisX = ellipsis.getAxisX();
		int axisY = ellipsis.getAxisY();
		double c = MathUtil.square(tangentDirection.x/ (double) tangentDirection.y)* axisY/(double) axisX;
		int tangentialPointX = (int) Math.round(axisX / Math.sqrt(1 + c) /2.0);
		int tangentialPointY = (int) Math.round(axisY / Math.sqrt(1 + 1/c) /2.0);
		return new Point(tangentialPointX*MathUtil.signum(tangentDirection.y), -tangentialPointY*MathUtil.signum(tangentDirection.x));
	}

	@Override
	public void buildOptionComposite(Composite parent) {
		Button ellipsisButton = new Button(parent, SWT.CHECK);
		ellipsisButton.setText("Render Ellipsis");
		ellipsisButton.setSelection(_showEllipsis);
		
		Button fillPolygonButton = new Button(parent, SWT.CHECK);
		fillPolygonButton.setText("Fill Polygon");
		fillPolygonButton.setSelection(_fillPolygon);
		
		Button fillEllipsisButton = new Button(parent, SWT.CHECK);
		fillEllipsisButton.setText("Fill Ellipsis");
		fillEllipsisButton.setSelection(_fillEllipsis);
		
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
		
		fillEllipsisButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selection = ((Button) e.getSource()).getSelection();
				setFillEllipsis(selection);
				RayModel.genericUpdate();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}
		});
	}
	
	public void setFillEllipsis(boolean fillEllipsis) {
		_fillEllipsis = fillEllipsis;
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
