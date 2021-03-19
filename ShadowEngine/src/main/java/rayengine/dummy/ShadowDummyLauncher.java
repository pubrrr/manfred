package rayengine.dummy;

import rayengine.dummy.databinding.RayModel;
import rayengine.dummy.ui.TestDisplay;

public class ShadowDummyLauncher {

	public static void main(String[] args) {
		RayModel.setInclination(30);
		new TestDisplay().openDisplay();
	}
}
