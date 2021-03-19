package rayengine.casters.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import rayengine.casters.Bounds;

public class BoundsTest {

	@Test
	public void testBoundsArePositive() {
		try {
			new Bounds(-3, -2);
			fail("Negative bounds should not be possible");
		} catch (IllegalArgumentException e) {
			//expected
		}
		
		try {
			new Bounds(5, -2);
			fail("Negative bounds should not be possible");
		} catch (IllegalArgumentException e) {
			//expected
		}
		
		try {
			new Bounds(-536546, 0);
			fail("Negative bounds should not be possible");
		} catch (IllegalArgumentException e) {
			//expected
		}
	}
	
	@Test
	public void testBoundsAreOrdered() {
		try {
			new Bounds(4564, 3);
			fail("Lower bound should be smaller than upper bound");
		} catch (IllegalArgumentException e) {
			//expected
		}
		
		try {
			new Bounds(6, 5);
			fail("Lower bound should be smaller than upper bound");
		} catch (IllegalArgumentException e) {
			//expected
		}
	}
	
	@Test
	public void testEmptyAlphaData() {
		Bounds emptyBounds = new Bounds(new byte[10], 0, 10);
		assertThat(emptyBounds.isEmpty()).isTrue();
		assertThat(new Bounds(2,4).isEmpty()).isFalse();
	}
	
	@Test
	public void testWidth() {
		assertThat(new Bounds(3,8).getWidth()).isEqualTo(6);
		assertThat(new Bounds(5,5).getWidth()).isEqualTo(1);
		assertThat(new Bounds(20,30).getWidth()).isEqualTo(11);
	}
	
	@Test
	public void testBoundsFromAlpha() {
		byte[] alphaData = new byte[]{0,0,1,3,0,0,0,34,1,1,3,4,4,0,5,54,4,0,0,3};
		
		Bounds fullBounds = new Bounds(alphaData, 0, 20);
		assertThat(fullBounds.getLowerBound()).isEqualTo(2);
		assertThat(fullBounds.getUpperBound()).isEqualTo(19);
		
		Bounds emptyBounds = new Bounds(alphaData, 0, 2);
		assertThat(emptyBounds.isEmpty()).isTrue();
		
		Bounds partialBounds1 = new Bounds(alphaData, 1, 5);
		assertThat(partialBounds1.getLowerBound()).isEqualTo(1);
		assertThat(partialBounds1.getUpperBound()).isEqualTo(2);
		
		Bounds partialBounds2 = new Bounds(alphaData, 8, 4);
		assertThat(partialBounds2.getLowerBound()).isEqualTo(0);
		assertThat(partialBounds2.getUpperBound()).isEqualTo(3);
		
		Bounds partialBounds3 = new Bounds(alphaData, 13, 5);
		assertThat(partialBounds3.getLowerBound()).isEqualTo(1);
		assertThat(partialBounds3.getUpperBound()).isEqualTo(3);
	}
	
	@Test
	public void testStartingIndexAndWidthWithinRange() {
		try {
			new Bounds(new byte[10], -1, -5);
			fail("Negative starting index and width are not allowed");
		} catch (IndexOutOfBoundsException e) {
			//expected
		}
		
		try {
			new Bounds(new byte[10], 1, -5);
			fail("Negative starting index and width are not allowed");
		} catch (IndexOutOfBoundsException e) {
			//expected
		}
		
		try {
			new Bounds(new byte[10], -1, 3);
			fail("Negative starting index and width are not allowed");
		} catch (IndexOutOfBoundsException e) {
			//expected
		}
		
		try {
			new Bounds(new byte[10], 2, 10);
			fail("Width and startingIndex have to be within range of alphaData");
		} catch (IndexOutOfBoundsException e) {
			//expected
		}
		
		try {
			new Bounds(new byte[10], 12, 2);
			fail("Width and startingIndex have to be within range of alphaData");
		} catch (IndexOutOfBoundsException e) {
			//expected
		}
		
		new Bounds(new byte[10], 2, 8);
	}
}
