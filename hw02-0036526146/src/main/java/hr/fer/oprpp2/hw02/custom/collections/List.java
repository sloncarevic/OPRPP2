package hr.fer.oprpp2.hw02.custom.collections;

/**
 * 
 *
 */
public interface List extends Collection {
	
	Object get(int index);
	
	void insert(Object value, int position);
	
	int indexOf(Object value);
	
	void remove(int index);


}
