package rayengine.test.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scale;

import rayengine.test.databinding.RayModel;

public class AltitudeScaleBuilder {

	public static Scale buildAltitudeScale(Composite parent) {
		Scale altitudeScale = new Scale(parent, SWT.VERTICAL);
		altitudeScale.setMaximum(90);
		altitudeScale.setMinimum(0);
		GridData altitudeScaleData = new GridData();
		altitudeScaleData.horizontalAlignment = SWT.CENTER;
		altitudeScaleData.grabExcessHorizontalSpace = true;
		altitudeScale.setLayoutData(altitudeScaleData);
		
		altitudeScale.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Scale scale = (Scale) e.getSource();
				int newAltitude = scale.getMaximum() - scale.getSelection();
				RayModel.setAltitude(newAltitude);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}
		});
		
		RayModel.addAltitudeListener(altitude -> altitudeScale.setSelection(altitudeScale.getMaximum() - altitude));
		
		return altitudeScale;
	}
}
