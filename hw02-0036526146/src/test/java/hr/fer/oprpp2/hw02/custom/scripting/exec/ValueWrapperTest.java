package hr.fer.oprpp2.hw02.custom.scripting.exec;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ValueWrapperTest {

	@Test
	void test1() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		v1.add(v2.getValue()); // v1 now stores Integer(0); v2 still stores null.
		assertEquals("0", v1.getValue().toString());
		assertEquals(null, v2.getValue());
	}
	
	@Test
	void test2() {
		ValueWrapper v3 = new ValueWrapper("1.2E1");
		ValueWrapper v4 = new ValueWrapper(Integer.valueOf(1));
		v3.add(v4.getValue()); // v3 now stores Double(13); v4 still stores Integer(1).
		assertEquals("13.0", v3.getValue().toString());
		assertEquals("1", v4.getValue().toString());
	}
	
	@Test
	void test3() {
		ValueWrapper v5 = new ValueWrapper("12");
		ValueWrapper v6 = new ValueWrapper(Integer.valueOf(1));
		v5.add(v6.getValue()); // v5 now stores Integer(13); v6 still stores Integer(1).
		assertEquals("13", v5.getValue().toString());
		assertEquals("1", v6.getValue().toString());

	}
	
	
	@Test
	void test4() {
		ValueWrapper v7 = new ValueWrapper("Ankica");
		ValueWrapper v8 = new ValueWrapper(Integer.valueOf(1));
		assertThrows(RuntimeException.class, () -> v7.add(v8.getValue())); // throws RuntimeException
	}
	

}
