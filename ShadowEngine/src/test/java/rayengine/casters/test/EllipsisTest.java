package rayengine.casters.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Point;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import rayengine.casters.Ellipsis;
import rayengine.dummy.MathUtil;
import rayengine.dummy.Pair;

public class EllipsisTest {

	@Test
	public void testNegativeDimensions() {
		try {
			new Ellipsis(-1, -400);
			fail("Negative dimensions should not be possible");
		} catch (IllegalArgumentException e) {
			//expected
		}
		
		try {
			new Ellipsis(2, -8);
			fail("Negative dimensions should not be possible");
		} catch (IllegalArgumentException e) {
			//expected
		}
		
		try {
			new Ellipsis(-43, 20);
			fail("Negative dimensions should not be possible");
		} catch (IllegalArgumentException e) {
			//expected
		}
	}
	
	@Test
	public void testContains() {
		Ellipsis ellipsis = new Ellipsis(-5, 10, 10, 20);
		assertThat(ellipsis.contains(new Point(5,10))).isFalse();
		assertThat(ellipsis.contains(new Point(0,0))).isTrue();
		assertThat(ellipsis.contains(new Point(0,9))).isTrue();
		assertThat(ellipsis.contains(new Point(-2,-4))).isTrue();
		assertThat(ellipsis.contains(new Point(6,0))).isFalse();
	}
	
	@Test
	public void testCalculateRelativeTangentialPoints() {
		Ellipsis ellipsis = new Ellipsis(10, 20);
		checkTangentPointsForTangentDirectionAndAngle(ellipsis, new Point(1,0), new Point(0, -10), 90);
		checkTangentPointsForTangentDirectionAndAngle(ellipsis, new Point(0,1), new Point(5, 0), 0);
		checkTangentPointsForTangentDirectionAndAngle(ellipsis, new Point(3, -8), new Point(-4,-6), 159);
		checkTangentPointsForTangentDirectionAndAngle(ellipsis, new Point(-2, 3), new Point(3,8), 326);
	}
	
	@Test
	public void testSquareTanAzimuthLeadsToSameResult() {
		Ellipsis ellipsis = new Ellipsis(10, 20);
		int azimuth = 242;
		double squareTanAzimuth = MathUtil.square(Math.tan((double) azimuth));
		Pair<Point> resultWithSquareTanAzimuth = ellipsis.calculateRelativeTangentialPoints(azimuth, Optional.of(squareTanAzimuth));
		Pair<Point> resultWithoutSquareTanAzimuth = ellipsis.calculateRelativeTangentialPoints(azimuth, Optional.empty());
		assertThat(resultWithoutSquareTanAzimuth).as("Adding square tangents of azimuth should lead to the same result since it is only an option to improve performance").isEqualTo(resultWithSquareTanAzimuth);
	}

	private void checkTangentPointsForTangentDirectionAndAngle(Ellipsis ellipsis, Point tangentDirection,
			Point expectedTangentialPoint1, int azimuth) {
		Point expectedTangentialPoint2 = new Point(-expectedTangentialPoint1.x, -expectedTangentialPoint1.y);
		Pair<Point> relativeTangentialPoints = ellipsis.calculateRelativeTangentialPoints(tangentDirection);
		checkPairContainsInOrder(relativeTangentialPoints, expectedTangentialPoint1, expectedTangentialPoint2);
		
		relativeTangentialPoints = ellipsis.calculateRelativeTangentialPoints(azimuth, Optional.empty());
		checkPairContainsInOrder(relativeTangentialPoints, expectedTangentialPoint1, expectedTangentialPoint2);
	}
	
	public void checkPairContainsInOrder(Pair<Point> points, Point point1, Point point2) {
		assertThat(points.getValue1()).isEqualTo(point1);
		assertThat(points.getValue2()).isEqualTo(point2);
	}
}
