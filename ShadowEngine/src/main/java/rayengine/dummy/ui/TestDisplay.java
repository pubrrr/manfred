package rayengine.dummy.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import rayengine.dummy.databinding.RayModel;

public class TestDisplay {
	
	public void openDisplay() {
		Display mainDisplay = new Display();
		Shell mainShell = new Shell(mainDisplay);
//		mainShell.setSize(700, 500);
		mainShell.setText("Ray Engine Test");
		mainShell.setLayout(new GridLayout(2, false));

		SceneCanvasBuilder.buildSzeneCanvas(mainShell);
		
		buildControlComposite(mainShell);

		RayModel.setAltitude(0);
		RayModel.setAzimuth(0);
		mainShell.layout();
		mainShell.setMinimumSize(mainShell.getSize());
		mainShell.open();
		while (!mainShell.isDisposed()) {
			if (!mainDisplay.readAndDispatch()) {
				mainDisplay.sleep();
			}
		}
		mainDisplay.dispose();
	}

	private void buildControlComposite(Shell mainShell) {
		Composite controlComposite = new Composite(mainShell, SWT.BORDER);
		controlComposite.setLayout(new GridLayout(1, false));
		GridData controlData = new GridData();
		controlData.grabExcessVerticalSpace = true;
		controlData.widthHint = 300;
		controlData.horizontalAlignment = SWT.RIGHT;
		controlData.verticalAlignment = SWT.FILL;
		controlComposite.setLayoutData(controlData);
		AngleTextCompositeBuilder.buildAngleTextComposite(controlComposite);
		buildAngleControlComposite(controlComposite);
		buildRayAlgorithmControlContribution(controlComposite);
	}

	private void buildAngleControlComposite(Composite parent) {
		Composite angleControlComposite = new Composite(parent, SWT.NONE);
		GridData altitudeControlData = new GridData();
		altitudeControlData.horizontalAlignment = SWT.FILL;
		altitudeControlData.grabExcessHorizontalSpace = true;
		angleControlComposite.setLayoutData(altitudeControlData);
		angleControlComposite.setLayout(new GridLayout(2, false));
		new AzimuthCanvasBuilder().buildAzimuthCanvas(angleControlComposite);
		AltitudeScaleBuilder.buildAltitudeScale(angleControlComposite);
	}
	
	private void buildRayAlgorithmControlContribution(Composite parent) {
		Composite rayAlgorithmControlContribution = new Composite(parent, SWT.NONE);
		GridData compositeGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		rayAlgorithmControlContribution.setLayoutData(compositeGridData);
		rayAlgorithmControlContribution.setLayout(new GridLayout(1, false));
		
		new RayAlgorithmComboBuilder().buildRayAlgorithmCombo(rayAlgorithmControlContribution);
	}
}
