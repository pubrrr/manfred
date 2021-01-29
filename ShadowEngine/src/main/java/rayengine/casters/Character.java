package rayengine.casters;

import org.eclipse.swt.widgets.Display;

public class Character extends AbstractCaster<Ellipsis>{
	
	public Character(String relavitePath, Display display) {
		super(relavitePath, display);
	}

	@Override
	protected void initializeSections(int height) {
		sections = new Ellipsis[height];
	}

	@Override
	protected Ellipsis createSection(int width, int height) {
		return new Ellipsis(width, height);
	}
}
