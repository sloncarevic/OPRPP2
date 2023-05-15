package hr.fer.oprpp2.hw02.custom.collections.demo;

import hr.fer.oprpp2.hw02.custom.collections.Tester;

public class EvenIntegerTester implements Tester{

	@Override
	public boolean test(Object obj) {
		if(!(obj instanceof Integer)) return false;
		Integer i = (Integer) obj;
		return i % 2 == 0;
	}

}
