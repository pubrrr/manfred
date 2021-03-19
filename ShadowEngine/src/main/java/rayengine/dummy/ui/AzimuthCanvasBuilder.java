package rayengine.dummy.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import rayengine.dummy.databinding.RayModel;

public class AzimuthCanvasBuilder {
	
	private boolean mouseClicked = false;

	public Canvas buildAzimuthCanvas(Composite parent) {
		Canvas azimuthCanvas = new Canvas(parent, SWT.BORDER);
		GridData azimuthCanvasData = new GridData();
		azimuthCanvasData.heightHint = 200;
		azimuthCanvasData.widthHint = 200;
		azimuthCanvas.addPaintListener(new AzimuthPaintListener());
		azimuthCanvas.setLayoutData(azimuthCanvasData);
		azimuthCanvas.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				mouseClicked(false);
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				mouseClicked(true);
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// do nothing
			}
		});
		azimuthCanvas.addMouseMoveListener(new MouseMoveListener() {
			private final double DEGREE_PER_RADIAN = 180 / Math.PI;
			
			@Override
			public void mouseMove(MouseEvent e) {
				if(mouseClicked) {
					int mouseXfromCenter = e.x - azimuthCanvas.getClientArea().width/2;
					int mouseYfromCenter = azimuthCanvas.getClientArea().height/2 - e.y;
					RayModel.setAzimuth(getAngle(mouseXfromCenter, mouseYfromCenter));
				}
			}
			
			private int getAngle(int x, int y) {
				if(y > 0) {
					int angle = (int) (Math.atan(x/ ((double) y)) * DEGREE_PER_RADIAN) ;
					if(angle < 0) {
						angle = 360 + angle;
					}
					return angle;
				} else if (y < 0) {
					int angle = 180 - (int) (Math.atan(x/ ((double) -y)) * DEGREE_PER_RADIAN) ;
					if(angle < 0) {
						angle = 180 + angle;
					}
					return angle;
				} else if (x > 0){
					return 90;
				} else {
					return 270;
				}
			}
		});
		RayModel.addAzimuthListener(azimuth -> azimuthCanvas.redraw());
		
		return azimuthCanvas;
	}
	
	private class AzimuthPaintListener implements PaintListener{
		
		private static final int MARGIN = 3;
		private static final double RADIANTS_PER_DEGREE = Math.PI/180;
		private static final int ARROW_HEAD_SIZE = 15;
		private static final int ARROW_THICKNESS = 3;

		@Override
		public void paintControl(PaintEvent e) {
			Canvas canvas = (Canvas) e.widget;
			Rectangle clientArea = canvas.getClientArea();
			int azimuth = RayModel.getAzimuth();
			int center = clientArea.width/2;
			int radius = clientArea.width/2 - MARGIN;
			int tipX = center + (int) (Math.sin(RADIANTS_PER_DEGREE * (double) azimuth)*radius);
			int tipY = center - (int) (Math.cos(RADIANTS_PER_DEGREE * (double) azimuth)*radius);
			int arrowHead1X = tipX - (int) (Math.sin(RADIANTS_PER_DEGREE * (double) (azimuth+45))*ARROW_HEAD_SIZE);
			int arrowHead1Y = tipY + (int) (Math.cos(RADIANTS_PER_DEGREE * (double) (azimuth+45))*ARROW_HEAD_SIZE);
			int arrowHead2X = tipX - (int) (Math.sin(RADIANTS_PER_DEGREE * (double) (azimuth-45))*ARROW_HEAD_SIZE);;
			int arrowHead2Y = tipY + (int) (Math.cos(RADIANTS_PER_DEGREE * (double) (azimuth-45))*ARROW_HEAD_SIZE);
			
			e.gc.drawOval(MARGIN, MARGIN, radius*2, radius*2);
			e.gc.setLineWidth(ARROW_THICKNESS);
			e.gc.drawLine(center, center, tipX, tipY);
			e.gc.drawLine(tipX, tipY, arrowHead1X, arrowHead1Y);
			e.gc.drawLine(tipX, tipY, arrowHead2X, arrowHead2Y);
		}
	}
	
	private void mouseClicked(boolean clicked) {
		mouseClicked = clicked;
	}
}
