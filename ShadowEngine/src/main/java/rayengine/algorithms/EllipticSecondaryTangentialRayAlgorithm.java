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

import rayengine.casters.AbstractCaster;
import rayengine.casters.AbstractSection;
import rayengine.casters.Character;
import rayengine.casters.Ellipsis;
import rayengine.dummy.MathUtil;
import rayengine.dummy.Pair;
import rayengine.dummy.databinding.RayModel;

public class EllipticSecondaryTangentialRayAlgorithm implements IShadowAlgorithm{

	private static final String NAME = "Elliptic Secondary Tangential";
	
	private boolean showEllipsis = true;
	private boolean fillPolygon = true;
	private boolean fillEllipsis = true;
	
	@Override
	public void castShadow(Canvas canvas, AbstractCaster<? extends AbstractSection> caster, GC gc) {
		gc.setForeground(canvas.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		gc.setBackground(canvas.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		int azimuth = RayModel.getAzimuth();
		int altitude = RayModel.getAltitude();
		caster.castSections(altitude, azimuth);
//		int leftBoundCoordinate = caster.getLeftBorderCoordinate();
//		int bottomBoundCoordinate = caster.getBottomBorderCoordinate();
//		AbstractSection[] sections = caster.getSections();
		if(showEllipsis) {
			if(fillEllipsis) {
				caster.drawOutlineSections(gc);
			} else {
				caster.drawFillSections(gc);
			}
		}
		
//		Map<Integer, Point[]> polygons = calculateSecondaryTangentialPolygonPoints(sections);
//		
//		if(_fillPolygon) {
//			for(Point[] polygon : polygons.values()) {
//				gc.fillPolygon(MathUtil.extractCoordinates(polygon, new Point(caster.getLeftBorderCoordinate(), caster.getBottomBorderCoordinate())));
//			}
//		} else {
//			gc.setForeground(canvas.getDisplay().getSystemColor(SWT.COLOR_RED));
//			for(Point[] polygon : polygons.values()) {
//				gc.drawLine(polygon[0].x+caster.getLeftBorderCoordinate(), polygon[0].y+caster.getBottomBorderCoordinate(), polygon[1].x+caster.getLeftBorderCoordinate(), polygon[1].y+caster.getBottomBorderCoordinate());
//				gc.drawLine(polygon[2].x+caster.getLeftBorderCoordinate(), polygon[2].y+caster.getBottomBorderCoordinate(), polygon[3].x+caster.getLeftBorderCoordinate(), polygon[3].y+caster.getBottomBorderCoordinate());
//			}
//		}
	}
	
//	private Map<Integer, Point[]> calculateSecondaryTangentialPolygonPoints(AbstractSection[] sections){
//		Map<Integer, Point[]> rowToPolygons = new HashMap<>();
//		for(int row=0; row < sections.length-1; row++) {
//			AbstractSection section = sections[row];
//			AbstractSection sectionBelow = sections[row+1];
//			if(!section.oneContainsTheOther(sectionBelow)) {
//				Point relativeCenter = section.calculateRelativeCenter(sectionBelow);
//				Point primaryTangentialPoint1 = section.calculateRelativeTangentialPoints(relativeCenter);
//				Point primaryTangentialPointBelow1 = calculateRelativeTangentialPoint(sectionBelow, relativeCenter);
//				Pair<Point> secondaryTangent1 = calculateAbsoluteSecondOrderTangentialPoints(section, sectionBelow, relativeCenter, primaryTangentialPoint1,
//						primaryTangentialPointBelow1);
//				Point primaryTangentialPoint2 = new Point(-primaryTangentialPoint1.x, -primaryTangentialPoint1.y);
//				Point primaryTangentialPointBelow2 = new Point(-primaryTangentialPointBelow1.x, -primaryTangentialPointBelow1.y);
//				relativeCenter.x *= -1;
//				relativeCenter.y *= -1;
//				Pair<Point> secondaryTangent2 = calculateAbsoluteSecondOrderTangentialPoints(sectionBelow, section, relativeCenter, primaryTangentialPointBelow2,
//						primaryTangentialPoint2);
//				rowToPolygons.put(row, new Point[] {secondaryTangent1.getValue1(), secondaryTangent1.getValue2(), secondaryTangent2.getValue1(), secondaryTangent2.getValue2()});
//			}
//		}
//		return rowToPolygons;
//	}
//
//	private Pair<Point> calculateAbsoluteSecondOrderTangentialPoints(Ellipsis section, Ellipsis sectionBelow, Point relativeCenter,
//			Point primaryTangentialPoint, Point primaryTangentialPointBelow) {
//		Point primaryTangent = MathUtil.add(relativeCenter, MathUtil.calculateRelativePosition(primaryTangentialPoint, primaryTangentialPointBelow));
//		Point secondaryTangentialPoint = calculateRelativeTangentialPoint(section, primaryTangent);
//		Point secondaryTangentialPointBelow = calculateRelativeTangentialPoint(sectionBelow, primaryTangent);
//		return new Pair<Point>(MathUtil.add(secondaryTangentialPoint, section.getCenter()), MathUtil.add(secondaryTangentialPointBelow, sectionBelow.getCenter()));
//	}

	@Override
	public void buildOptionComposite(Composite parent) {
		Button ellipsisButton = new Button(parent, SWT.CHECK);
		ellipsisButton.setText("Render Ellipsis");
		ellipsisButton.setSelection(showEllipsis);
		
		Button fillPolygonButton = new Button(parent, SWT.CHECK);
		fillPolygonButton.setText("Fill Polygon");
		fillPolygonButton.setSelection(fillPolygon);
		
		Button fillEllipsisButton = new Button(parent, SWT.CHECK);
		fillEllipsisButton.setText("Fill Ellipsis");
		fillEllipsisButton.setSelection(fillEllipsis);
		
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
		this.fillEllipsis = fillEllipsis;
	}
	
	public void setShowEllipsis(boolean showEllipsis) {
		this.showEllipsis = showEllipsis;
	}

	public void setFillPolygon(boolean fillPolygon) {
		this.fillPolygon = fillPolygon;
	}

	@Override
	public String getName() {
		return NAME;
	}
}
