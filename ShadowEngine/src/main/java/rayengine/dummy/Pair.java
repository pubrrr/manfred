package rayengine.dummy;

public class Pair<K> {
	K value1;
	K value2;

	public Pair(K value1, K value2){
		this.value1 = value1;
		this.value2 = value2;
	}
	
	public void swap() {
		K temp = value1;
		value1 = value2;
		value2 = temp;
	}
	
	public K getValue1() {
		return value1;
	}

	public void setValue1(K value1) {
		this.value1 = value1;
	}

	public K getValue2() {
		return value2;
	}

	public void setValue2(K value2) {
		this.value2 = value2;
	}
}
