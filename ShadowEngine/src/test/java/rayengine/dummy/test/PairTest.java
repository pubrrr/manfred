package rayengine.dummy.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import rayengine.dummy.Pair;

public class PairTest {

	@Test
	public void testSwap() {
		Integer firstValue = Integer.valueOf(4332);
		Integer secondValue = Integer.valueOf(-565435);
		Pair<Integer> integerPair = new Pair<>(firstValue, secondValue);
		assertThat(integerPair.getValue1()).isEqualTo(firstValue);
		assertThat(integerPair.getValue2()).isEqualTo(secondValue);
		integerPair.swap();
		assertThat(integerPair.getValue1()).isEqualTo(secondValue);
		assertThat(integerPair.getValue2()).isEqualTo(firstValue);
	}
}
