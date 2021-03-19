package rayengine.casters;

import static rayengine.dummy.MathUtil.RADIANTS_PER_DEGREE;
import static rayengine.dummy.MathUtil.square;

import java.awt.Point;
import java.util.Optional;

import rayengine.dummy.MathUtil;
import rayengine.dummy.Pair;

public class Ellipsis extends AbstractSection{

	public Ellipsis(int bottomBorder, int leftBorder, int width, int height) {
		super(bottomBorder, leftBorder, width, height);
	}
	
	public Ellipsis(int width, int height) {
		super(width, height);
	}
	
	@Override
	public boolean contains(Point point) {
		if(width == 0 || height == 0) {
			return false;
		}
		Point relativePosition = MathUtil.calculateRelativePosition(getCenter(), point);
		return 4*(square(relativePosition.x/(double) width) + square(relativePosition.y/(double) height)) <= 1;
	}
	
	@Override
	public boolean internalOneContainsTheOther(AbstractSection otherEllipsis) {
		Pair<Ellipsis> sections = new Pair<Ellipsis>(this, (Ellipsis) otherEllipsis);
		boolean sorted = sortBySize(sections);
		if(sorted) {
			Ellipsis smallEllipsis = sections.getValue1();
			Ellipsis bigEllipsis = sections.getValue2();
			Ellipsis centerContainmentEllipsis = new Ellipsis(bigEllipsis.getWidth()-smallEllipsis.getWidth(), bigEllipsis.getHeight()-smallEllipsis.getHeight());
			Point relativeSmallCenter = bigEllipsis.calculateRelativeCenter(smallEllipsis);
			return centerContainmentEllipsis.contains(relativeSmallCenter);
		}
		return false;
	}
	
	@Override
	protected ISectionDrawConsumer getDrawFillConsumer() {
		return (gc, x, y, w, h) -> gc.fillOval(x, y, w, h);
	}

	@Override
	protected ISectionDrawConsumer getDrawOutlineConsumer() {
		return (gc, x, y, w, h) -> gc.drawOval(x, y, w, h);
	}
	
	public Pair<Point> calculateRelativeTangentialPoints(Point tangentDirection) {
		double c = MathUtil.square(tangentDirection.x*height/ (double) (tangentDirection.y*width));
		Point firstTangentialPoint = getUnorientedTangentialPoint(c);
		orientTangentialPoint(firstTangentialPoint, tangentDirection);
		return createOpposingPoints(firstTangentialPoint);
	}
	
	public Pair<Point> calculateRelativeTangentialPoints(int azimuth, Optional<Double> squareTanOfAzimuthIn) {
		double squareTanOfAzimuth = squareTanOfAzimuthIn.orElse(MathUtil.square(Math.tan(RADIANTS_PER_DEGREE*azimuth)));
		double c = squareTanOfAzimuth*MathUtil.square(height/((double) width));
		Point firstTangentialPoint = getUnorientedTangentialPoint(c);
		orientTangentialPoint(firstTangentialPoint, azimuth);
		return createOpposingPoints(firstTangentialPoint);
	}
	
	private void orientTangentialPoint(Point tangentialPoint, int azimuth) {
		if(azimuth > 90 && azimuth < 270) {
			tangentialPoint.x = -tangentialPoint.x;
		}
		if(azimuth < 180) {
			tangentialPoint.y = -tangentialPoint.y;
		}
	}
	
	private Pair<Point> createOpposingPoints(Point point){
		return new Pair<>(point, new Point(-point.x, -point.y));
	}
	
	//tangents point anti-clockwise
	private void orientTangentialPoint(Point tangentialPoint, Point tangentDirection) {
		tangentialPoint.x = tangentialPoint.x*MathUtil.signum(tangentDirection.y);
		tangentialPoint.y = -tangentialPoint.y*MathUtil.signum(tangentDirection.x);
	}
	
	private Point getUnorientedTangentialPoint(double cFactor) {
		return new Point((int) Math.round(width / Math.sqrt(1 + cFactor) /2.0), (int) Math.round(height / Math.sqrt(1 + 1/cFactor) /2.0));
	}

	@Override
	public Pair<Pair<Point>> calculateCommonTangents(AbstractSection otherSection) {
		
		throw new IllegalArgumentException("Fick deine Mutter");
	}
}
