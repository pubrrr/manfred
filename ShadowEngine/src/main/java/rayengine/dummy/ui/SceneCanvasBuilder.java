package rayengine.dummy.ui;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Shell;

import rayengine.algorithms.IShadowAlgorithm;
import rayengine.casters.Character;
import rayengine.dummy.databinding.IAngleListener;
import rayengine.dummy.databinding.RayModel;

public class SceneCanvasBuilder {
	
	public static Canvas buildSzeneCanvas(Shell mainShell) {
		Canvas szeneCanvas = new Canvas(mainShell, SWT.BORDER | SWT.DOUBLE_BUFFERED);
		szeneCanvas.setLayout(new FillLayout());
		GridData szeneCanvasData = new GridData(SWT.FILL, SWT.FILL, true, true);
		szeneCanvasData.minimumHeight = 500;
		szeneCanvasData.minimumWidth = 500;
		szeneCanvas.setLayoutData(szeneCanvasData);
		
		Character character = new Character("resources" + File.separatorChar + "test1.png", mainShell.getDisplay());
		szeneCanvas.addPaintListener(new PaintListener() {
			
			
			@Override
			public void paintControl(PaintEvent e) {
				Point canvasCenter = new Point(szeneCanvas.getClientArea().width/2, szeneCanvas.getClientArea().height/2);
				character.setLeftBorderCoordinate(canvasCenter.x - character.getDisplayedWidth()/2);
				character.setBottomBorderCoordinate(canvasCenter.y + character.getDisplayedHeight()/2);
				IShadowAlgorithm rayAlgorithm = RayModel.getRayAlgorithm();
				if(rayAlgorithm != null) {
					rayAlgorithm.castShadow(szeneCanvas, character, e.gc);
				}
				character.draw(e.gc);
				if(rayAlgorithm != null) {
					character.castShadowOnSelf(e.gc);
				}
			}
		});
		
		RayModel.addAngleListener(new IAngleListener() {
			
			@Override
			public void updateAltitude(int altitude) {
				szeneCanvas.redraw();
			}
			
			@Override
			public void updateAzimuth(int azimuth) {
				szeneCanvas.redraw();
			}
		});
		
		RayModel.addGenericUpdateListener(szeneCanvas::redraw);
		
		return szeneCanvas;
	}
}
