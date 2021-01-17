package rayengine.test;

public class Pair<K> {
	K _value1;
	K _value2;

	public Pair(K value1, K value2){
		_value1 = value1;
		_value2 = value2;
	}
	
	public void swap() {
		K temp = _value1;
		_value1 = _value2;
		_value2 = temp;
	}
	
	public K getValue1() {
		return _value1;
	}

	public void setValue1(K _value1) {
		this._value1 = _value1;
	}

	public K getValue2() {
		return _value2;
	}

	public void setValue2(K _value2) {
		this._value2 = _value2;
	}
}
