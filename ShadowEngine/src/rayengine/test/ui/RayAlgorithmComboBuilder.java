package rayengine.test.ui;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import rayengine.algorithms.EllipticRayAlgorithm;
import rayengine.algorithms.EllipticSecondaryTangentialRayAlgorithm;
import rayengine.algorithms.EllipticTangentialRayAlgorithm;
import rayengine.algorithms.IRayAlgorithm;
import rayengine.algorithms.SlidingEllipsesRayAlgorithm;
import rayengine.test.databinding.RayModel;

public class RayAlgorithmComboBuilder {
	
	private Composite optionComposite;

	public Composite buildRayAlgorithmCombo(Composite parent) {
		Combo algorithmCombo = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
		Set<IRayAlgorithm> rayAlgorithms = new HashSet<IRayAlgorithm>();
		rayAlgorithms.add(new EllipticRayAlgorithm());
		rayAlgorithms.add(new EllipticTangentialRayAlgorithm());
		rayAlgorithms.add(new SlidingEllipsesRayAlgorithm());
		rayAlgorithms.add(new EllipticSecondaryTangentialRayAlgorithm());
		rayAlgorithms.forEach(algorithm -> algorithmCombo.add(algorithm.getName()));
		algorithmCombo.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo combo = (Combo) e.getSource();
				rayAlgorithms.stream()
										.filter(algorithm -> algorithm.getName().equals(combo.getText()))
										.findAny()
										.ifPresent(RayAlgorithmComboBuilder.this::selectRayAlgorithm);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}
		});
		
		optionComposite = new Composite(parent, SWT.NONE);
		optionComposite.setLayout(new GridLayout(1, false));
		optionComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		return algorithmCombo;
	}
	
	private void selectRayAlgorithm(IRayAlgorithm algorithm) {
		RayModel.setRayAlgorithm(algorithm);
		for(Control child : optionComposite.getChildren()) {
			child.dispose();
		}
		algorithm.buildOptionComposite(optionComposite);
		optionComposite.layout(true);
	}
}
