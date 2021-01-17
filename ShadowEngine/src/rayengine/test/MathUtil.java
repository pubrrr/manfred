package rayengine.test;

import org.eclipse.swt.graphics.Point;

public class MathUtil {

	public static final double RADIANTS_PER_DEGREE = Math.PI/180;
	
	public static int signum(int number) {
		if(number > 0) {
			return 1;
		} else if(number < 0){
			return -1;
		} else {
			return 0;
		}
	}

	public static double square(double x) {
		return x*x;
	}
	
	public static Point calculateRelativePosition(Point point1, Point point2) {
		return new Point(point2.x - point1.x, point2.y - point1.y);
	}
	
	public static Point add(Point point1, Point point2) {
		return new Point(point2.x + point1.x, point2.y + point1.y);
	}
	
	public static int[] extractCoordinates(Point[] points, Point offset) {
		int[] coordinates = new int[points.length*2];
		for(int i = 0; i<points.length; i++) {
			coordinates[2*i] = points[i].x + offset.x;
			coordinates[2*i+1] = points[i].y + offset.y;
		}
		return coordinates;
	}
}
