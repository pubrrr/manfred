package rayengine.casters;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

public abstract class AbstractSection {
	protected int bottomBorder;
	protected int leftBorder;
	protected int width;
	protected int height;
	
	public AbstractSection(int bottomBorder, int leftBorder, int width, int height) {
		this.bottomBorder = bottomBorder;
		this.leftBorder = leftBorder;
		this.width = width;
		this.height = height;
	}
	
	public AbstractSection(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public boolean contains(Point point) {
		return contains(point.x, point.y);
	}
	
	public void setBorderCoordinates(int leftBound, int bottomBound) {
		leftBorder = leftBound;
		bottomBorder = bottomBound;
	}
	
	public Point getCenter() {
		return new Point(leftBorder + width/2, bottomBorder - height/2);
	}

	public int getBottomBound() {
		return bottomBorder;
	}

	public int getLeftBound() {
		return leftBorder;
	}
	
	public int getAxisX() {
		return width;
	}

	public int getAxisY() {
		return height;
	}
	
	public abstract boolean contains(int x, int y);
	
	public abstract void draw(GC gc);
}
