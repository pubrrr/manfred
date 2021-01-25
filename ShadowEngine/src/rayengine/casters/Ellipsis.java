package rayengine.casters;

import static rayengine.test.MathUtil.square;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import rayengine.test.MathUtil;
import rayengine.test.Pair;

public class Ellipsis extends AbstractSection{

	public Ellipsis(int bottomBorder, int leftBorder, int width, int height) {
		super(bottomBorder, leftBorder, width, height);
	}
	
	public Ellipsis(int width, int height) {
		super(width, height);
	}
	
	public boolean contains(Point point) {
		return contains(point.x, point.y);
	}
	
	@Override
	public boolean contains(int x, int y) {
		if(width == 0 || height == 0) {
			return false;
		}
		return 4*(square(x/(double) width) + square(y/(double) height)) <= 1;
	}
	
	public static boolean oneContainsTheOther(Ellipsis ellipsis1, Ellipsis ellipsis2) {
		Pair<Ellipsis> sections = new Pair<Ellipsis>(ellipsis1, ellipsis2);
		boolean sorted = sortBySize(sections);
		if(sorted) {
			Ellipsis smallEllipsis = sections.getValue1();
			Ellipsis bigEllipsis = sections.getValue2();
			Ellipsis centerContainmentEllipsis = new Ellipsis(bigEllipsis.getAxisX()-smallEllipsis.getAxisX(), bigEllipsis.getAxisY()-smallEllipsis.getAxisY());
			Point relativeSmallCenter = calculateRelativeCenter(bigEllipsis, smallEllipsis);
			return centerContainmentEllipsis.contains(relativeSmallCenter);
		}
		return false;
	}
	
	public static boolean sortBySize(Pair<Ellipsis> sections) {
		boolean sorted = false;
		if(sections.getValue1().getAxisX() > sections.getValue2().getAxisX() && sections.getValue1().getAxisY() > sections.getValue2().getAxisY()) {
			sections.swap();
			sorted = true;
		} else if(sections.getValue1().getAxisX() <= sections.getValue2().getAxisX() && sections.getValue1().getAxisY() <= sections.getValue2().getAxisY()) {
			sorted = true;
		}
		return sorted;
	}
	
	public static Point calculateRelativeCenter(Ellipsis ellipsis1, Ellipsis ellipsis2) {
		return MathUtil.calculateRelativePosition(ellipsis1.getCenter(), ellipsis2.getCenter());
	}
	
	@Override
	public void draw(GC gc) {
		gc.fillOval(leftBorder, bottomBorder-height, width, height);
	}
}
