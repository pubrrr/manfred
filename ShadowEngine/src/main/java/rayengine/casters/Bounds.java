package rayengine.casters;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Bounds{
	private int lowerBound = -1;
	private int upperBound = -1;
	
	public Bounds(int lowerBound, int upperBound) {
		if(lowerBound > -1 && upperBound >= lowerBound) {
			this.lowerBound = lowerBound;
			this.upperBound = upperBound;
		} else {
			throw new IllegalArgumentException("Bounds have to be positive and upper bound has to be larger than lower bound (or equal).");
		}
	}
	
	public Bounds(byte[] alphaData, int startingIndex, int width) {
		if(startingIndex > -1 && width > 0 && startingIndex+width <= alphaData.length) {
			lowerBound = getFirstNonTransparentIndex(alphaData, startingIndex, startingIndex + width, true);
			if(lowerBound != -1) {
				lowerBound -= startingIndex;
				upperBound = getFirstNonTransparentIndex(alphaData, startingIndex + lowerBound, startingIndex + width, false) - startingIndex;
			}
		} else {
			throw new IndexOutOfBoundsException();
		}
		
	}
	
	private int getFirstNonTransparentIndex(byte[] alphaData, int smallestIndex, int biggestIndex, boolean fromLeftToRight) {
		Stream<Integer> iteratingIndices = IntStream.range(smallestIndex,biggestIndex)
																									.boxed();
		if(!fromLeftToRight) {
			iteratingIndices = iteratingIndices.sorted(Collections.reverseOrder());
		}
		return iteratingIndices.filter(i -> alphaData[i] != 0)
														.findFirst()
														.orElse(-1);
	}

	public int getLowerBound() {
		return lowerBound;
	}

	public int getUpperBound() {
		return upperBound;
	}
	
	public int getWidth() {
		return upperBound - lowerBound + 1;
	}
	
	public boolean isEmpty() {
		return lowerBound == -1 && upperBound == -1;
	}
	
	@Override
	public String toString() {
		return MessageFormat.format("[{0}-{1}]", lowerBound, upperBound);
	}
}
