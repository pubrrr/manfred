package rayengine.casters;

import org.eclipse.swt.widgets.Display;

import rayengine.test.databinding.RayModel;

public class Character extends AbstractCaster{
	
	public Character(String relavitePath, Display display) {
		super(relavitePath, display);
	}

	@Override
	protected void calculateSections() {
		int pixelSize = RayModel.getPixelSize();
		sections = new Ellipsis[height];
		
		for(int row = 0; row < height; row++) {
			int width = leftToRightBounds[row].getWidth()*pixelSize;
			int height = (int) Math.round(frontToBackBounds[row].getWidth()*pixelSize*RayModel.getInclinationFactor());
			sections[row] = new Ellipsis(width, height);
		}
	}
}
