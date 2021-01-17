package rayengine.casters;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import rayengine.test.MathUtil;
import rayengine.test.Pair;

public class Ellipsis {

	private int _bottomBound;
	private int _leftBound;
	private int _axisX;
	private int _axisY;
	
	public Ellipsis(int bottomBound, int leftBound, int axisX, int axisY) {
		_bottomBound = bottomBound;
		_leftBound = leftBound;
		_axisX = axisX;
		_axisY = axisY;
	}
	
	public Ellipsis(int axisX, int axisY) {
		_axisX = axisX;
		_axisY = axisY;
	}
	
	public boolean contains(Point point) {
		return contains(point.x, point.y);
	}
	
	public boolean contains(int x, int y) {
		if(_axisX == 0 || _axisY == 0) {
			return false;
		}
		return 4*x*x/((double) _axisX*_axisX) + 4*y*y/((double) _axisY*_axisY) <= 1;
	}
	
	public Point getCenter() {
		return new Point(_leftBound + _axisX/2, _bottomBound - _axisY/2);
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
	
	public void setBounds(int leftBound, int bottomBound) {
		_leftBound = leftBound;
		_bottomBound = bottomBound;
	}

	public int getBottomBound() {
		return _bottomBound;
	}

	public int getLeftBound() {
		return _leftBound;
	}
	
	public int getAxisX() {
		return _axisX;
	}

	public int getAxisY() {
		return _axisY;
	}
	
	public void draw(GC gc) {
		gc.fillOval(_leftBound, _bottomBound-_axisY, _axisX, _axisY);
	}
}
