package rayengine.algorithms;

import static rayengine.dummy.MathUtil.RADIANTS_PER_DEGREE;

import java.awt.Point;
import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import rayengine.casters.AbstractCaster;
import rayengine.casters.AbstractSection;
import rayengine.casters.Ellipsis;
import rayengine.dummy.MathUtil;
import rayengine.dummy.Pair;
import rayengine.dummy.databinding.RayModel;

public class SectionTangentialAlgorithm implements IShadowAlgorithm{

	private static final String NAME = "Section Tangential";
	
	private boolean showEllipsis = true;
	private boolean fillPolygon = true;
	
	@Override
	public void castShadow(Canvas canvas, AbstractCaster<? extends AbstractSection> caster, GC gc) {
		gc.setBackground(canvas.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		int azimuth = RayModel.getAzimuth();
		int altitude = RayModel.getAltitude();
		caster.castSections(altitude, azimuth);
		if(showEllipsis) {
			caster.drawFillSections(gc);
		}
		
		Optional<Double> squareTanOfAzimuth = Optional.of(MathUtil.square(Math.tan(RADIANTS_PER_DEGREE*azimuth)));
		int[] polygonCoordinates = calculateTangentialPointsPolygon(caster.getSections(), caster.getBottomLeftCoordinate(), azimuth, squareTanOfAzimuth);
		
		if(fillPolygon) {
			gc.setForeground(canvas.getDisplay().getSystemColor(SWT.COLOR_BLACK));
			gc.fillPolygon(polygonCoordinates);
		} else {
			gc.setForeground(canvas.getDisplay().getSystemColor(SWT.COLOR_RED));
			gc.drawPolygon(polygonCoordinates);
		}
	}

	private int[] calculateTangentialPointsPolygon(AbstractSection[] sections, Point characterBottomLeft, int azimuth,
			Optional<Double> squareTanOfAzimuth) {
		int[] polygonCoordinates = new int[sections.length*4];

		for(int row = 0; row < sections.length; row++) {
			Ellipsis ellipsis = (Ellipsis) sections[row];
			Pair<Point> relativeTangentialPoints = ellipsis.calculateRelativeTangentialPoints(azimuth, squareTanOfAzimuth);
			Point tangetialPoint1 = ellipsis.getAbsolute(MathUtil.filpY(relativeTangentialPoints.getValue1()));
			Point tangetialPoint2 = ellipsis.getAbsolute(MathUtil.filpY(relativeTangentialPoints.getValue2()));
			polygonCoordinates[row*2] = tangetialPoint1.x + characterBottomLeft.x;
			polygonCoordinates[row*2+1] = tangetialPoint1.y + characterBottomLeft.y;
			polygonCoordinates[polygonCoordinates.length - 2 - row*2] = tangetialPoint2.x + characterBottomLeft.x;
			polygonCoordinates[polygonCoordinates.length - 1 - row*2] = tangetialPoint2.y + characterBottomLeft.y;
		}
		return polygonCoordinates;
	}
	
	@Override
	public void buildOptionComposite(Composite parent) {
		Button ellipsisButton = new Button(parent, SWT.CHECK);
		ellipsisButton.setText("Render Ellipsis");
		ellipsisButton.setSelection(showEllipsis);
		
		Button fillPolygonButton = new Button(parent, SWT.CHECK);
		fillPolygonButton.setText("Fill Polygon");
		fillPolygonButton.setSelection(fillPolygon);
		
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
