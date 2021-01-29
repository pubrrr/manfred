package rayengine.dummy.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Point;

import org.junit.jupiter.api.Test;

import rayengine.dummy.MathUtil;

public class MathUtilTest {

	@Test
	public void testCalculateRelativePosition() {
		checkRelativePosition(1, 2, 3, 4);
		checkRelativePosition(5, 444, 2,88987);
		checkRelativePosition(-453543, 8887, 22543, 65335);
		checkRelativePosition(0, -22445, -23333, -4);
	}
	
	@Test
	public void testVectorSum() {
		checkVectorSum(1, 2, 3, 4);
		checkVectorSum(5, 444, 2,88987);
		checkVectorSum(-453543, 8887, 22543, 65335);
		checkVectorSum(0, -22445, -23333, -4);
	}
	
	@Test
	public void testSignum() {
		assertThat(MathUtil.signum(-205435)).isEqualTo(-1);
		assertThat(MathUtil.signum(-1)).isEqualTo(-1);
		assertThat(MathUtil.signum(-10)).isEqualTo(-1);
		assertThat(MathUtil.signum(0)).isEqualTo(0);
		assertThat(MathUtil.signum(1)).isEqualTo(1);
		assertThat(MathUtil.signum(875654)).isEqualTo(1);
		assertThat(MathUtil.signum(55)).isEqualTo(1);
	}

	private void checkRelativePosition(int x1, int y1, int x2, int y2) {
		Point point1 = new Point(x1, y1);
		Point point2 = new Point(x2, y2);
		Point relativePosition = MathUtil.calculateRelativePosition(point1, point2);
		assertThat(relativePosition.x).isEqualTo(point2.x - point1.x);
		assertThat(relativePosition.y).isEqualTo(point2.y - point1.y);
	}

	private void checkVectorSum(int x1, int y1, int x2, int y2) {
		Point point1 = new Point(x1, y1);
		Point point2 = new Point(x2, y2);
		Point vectorSum = MathUtil.add(point1, point2);
		assertThat(vectorSum.x).isEqualTo(point2.x + point1.x);
		assertThat(vectorSum.y).isEqualTo(point2.y + point1.y);
	}
}
