package rayengine.casters;

import java.awt.Point;

import org.eclipse.swt.graphics.GC;

import rayengine.dummy.MathUtil;
import rayengine.dummy.Pair;

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
	
	public void setBorderCoordinates(int leftBorder, int bottomBorder) {
		this.leftBorder = leftBorder;
		this.bottomBorder = bottomBorder;
	}
	
	public Point getCenter() {
		return new Point(leftBorder + width/2, bottomBorder - height/2);
	}

	public int getBottomBorder() {
		return bottomBorder;
	}

	public int getLeftBorder() {
		return leftBorder;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public abstract boolean contains(int x, int y);
	
	public void drawFill(GC gc, Point displacement) {
		draw(gc, displacement, getDrawFillConsumer());
	}
	
	public void drawOutline(GC gc, Point displacement) {
		draw(gc, displacement, getDrawOutlineConsumer());
	}
	
	private void draw(GC gc, Point displacement, ISectionDrawConsumer sectionDrawConsumer) {
		sectionDrawConsumer.drawSection(gc, displacement.x + leftBorder, displacement.y + bottomBorder - height, width, height);
	}
	
	public static boolean sortBySize(Pair<? extends AbstractSection> sections) {
		boolean sorted = false;
		if(sections.getValue1().getWidth() > sections.getValue2().getWidth() && sections.getValue1().getHeight() > sections.getValue2().getHeight()) {
			sections.swap();
			sorted = true;
		} else if(sections.getValue1().getWidth() <= sections.getValue2().getWidth() && sections.getValue1().getHeight() <= sections.getValue2().getHeight()) {
			sorted = true;
		}
		return sorted;
	}
	
	public Point calculateRelativeCenter(AbstractSection otherSection) {
		return MathUtil.calculateRelativePosition(getCenter(), otherSection.getCenter());
	}
	
	public Point getAbsolute(Point relativeCoordinate) {
		return MathUtil.add(getCenter(), relativeCoordinate);
	}
	
	protected abstract ISectionDrawConsumer getDrawFillConsumer();
	
	protected abstract ISectionDrawConsumer getDrawOutlineConsumer();
	
	public abstract Pair<Pair<Point>> calculateCommonTangents(AbstractSection otherSection);
	
	public final boolean oneContainsTheOther(AbstractSection otherSection) {
		if(otherSection.getClass().equals(this.getClass())){
			return doOneContainsTheOther(otherSection);
		}
		throw new IllegalArgumentException("Fick deine Mama");
	}

	protected abstract boolean doOneContainsTheOther(AbstractSection otherSection);
}
