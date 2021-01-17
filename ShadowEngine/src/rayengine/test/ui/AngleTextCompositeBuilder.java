package rayengine.test.ui;

import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import rayengine.test.databinding.RayModel;

public class AngleTextCompositeBuilder {

	public static Composite buildAngleTextComposite(Composite parent) {
		Composite angleTextComposite = new Composite(parent, SWT.NONE);
		angleTextComposite.setLayout(new GridLayout(2, false));
		GridData angleTextLayoutData = new GridData();
		angleTextLayoutData.grabExcessHorizontalSpace = true;
		angleTextLayoutData.horizontalAlignment = SWT.FILL;
		angleTextComposite.setLayoutData(angleTextLayoutData);
		Label azimuthLabel = new Label(angleTextComposite, SWT.NONE);
		azimuthLabel.setText("Azimuth:");
		azimuthLabel.setToolTipText("Das ist der Winkel in der Horizontalen, du Spast");
		Text azimuthText = createAzimuthText(angleTextComposite);
		azimuthText.setLayoutData(angleTextLayoutData);
		Label altitudeLabel = new Label(angleTextComposite, SWT.NONE);
		altitudeLabel.setText("Altitude:");
		altitudeLabel.setToolTipText("Das ist der Winkel in der Vertikalen, du Spast");
		Text altitudeText = createAltitudeText(angleTextComposite);
		altitudeText.setLayoutData(angleTextLayoutData);
		return angleTextComposite;
	}
	
	private static Text createAzimuthText(Composite parent) {
		Text azimuthText = new Text(parent, SWT.SINGLE);
		addTextInputListeners(azimuthText, 360, RayModel::setAzimuth);
		RayModel.addAzimuthListener(azimuth -> azimuthText.setText(String.valueOf(azimuth)));
		return azimuthText;
	}
	
	private static Text createAltitudeText(Composite parent) {
		Text altitudeText = new Text(parent, SWT.SINGLE);
		addTextInputListeners(altitudeText, 90, RayModel::setAltitude);
		RayModel.addAltitudeListener(altitude -> altitudeText.setText(String.valueOf(altitude)));
		return altitudeText;
	}

	private static void addTextInputListeners(Text altitudeText, int upperBound, Consumer<Integer> setValue) {
		altitudeText.addVerifyListener(new VerifyListener() {
			
			@Override
			public void verifyText(VerifyEvent e) {
				String oldContent = ((Text) e.widget).getText();
				String newContent = oldContent.substring(0, e.start) + e.text + oldContent.substring(e.end);
				int newAzimuth;
				try{
					newAzimuth = Integer.parseInt(newContent);
				} catch (NumberFormatException exception) {
					e.doit = !oldContent.equals(newContent) && newContent.isEmpty();
					return;
				}
				e.doit = !oldContent.equals(newContent) && newAzimuth >= 0 && newAzimuth <= upperBound;
			}
		});
		altitudeText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				String content = ((Text) e.getSource()).getText();
				int newAzimuth;
				try {
					newAzimuth = Integer.parseInt(content);
				} catch (NumberFormatException exception) {
					return;
				}
				setValue.accept(newAzimuth);
			}
		});
	}
}
