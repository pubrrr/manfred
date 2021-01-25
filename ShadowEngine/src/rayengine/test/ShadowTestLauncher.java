package rayengine.test;

import rayengine.test.databinding.RayModel;
import rayengine.test.ui.TestDisplay;

public class ShadowTestLauncher {

	public static void main(String[] args) {
		RayModel.setInclination(30);
		new TestDisplay().openDisplay();
	}
}
