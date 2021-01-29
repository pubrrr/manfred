package rayengine.dummy.ui;

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

import rayengine.algorithms.SimpleSectionAlgorithm;
import rayengine.algorithms.EllipticSecondaryTangentialRayAlgorithm;
import rayengine.algorithms.SectionTangentialAlgorithm;
import rayengine.algorithms.IShadowAlgorithm;
import rayengine.algorithms.SlidingEllipsesRayAlgorithm;
import rayengine.dummy.databinding.RayModel;

public class RayAlgorithmComboBuilder {
	
	private Composite optionComposite;

	public Composite buildRayAlgorithmCombo(Composite parent) {
		Combo algorithmCombo = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
		Set<IShadowAlgorithm> rayAlgorithms = new HashSet<IShadowAlgorithm>();
		rayAlgorithms.add(new SimpleSectionAlgorithm());
		rayAlgorithms.add(new SectionTangentialAlgorithm());
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
	
	private void selectRayAlgorithm(IShadowAlgorithm algorithm) {
		RayModel.setRayAlgorithm(algorithm);
		for(Control child : optionComposite.getChildren()) {
			child.dispose();
		}
		algorithm.buildOptionComposite(optionComposite);
		optionComposite.layout(true);
	}
}
