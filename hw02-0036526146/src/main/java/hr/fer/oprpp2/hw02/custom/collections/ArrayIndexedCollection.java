package hr.fer.oprpp2.hw02.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Implementation of resizable array-backed collection of objects
 *
 */
public class ArrayIndexedCollection implements List {
	
	private Object[] elements;		//array of object references
	private int size;				//current size of collection
	
	private static final int DEFAULT_CAPACITY = 16;	//default capacity
	
	private long modificationCount;	//change at add, insert, remove, clear
	
	/**
	 * Default constructor
	 */
	public ArrayIndexedCollection() {
		this(DEFAULT_CAPACITY);
	}
	
	/**
	 * @param initalCapacity 
	 * @throws IllegalArgumentException when initalCapacity is less than 1
	 */
	public ArrayIndexedCollection(int initalCapacity) {
		if (initalCapacity < 1) throw new IllegalArgumentException("Initial capacity is less than 1");
		this.elements = new Object[initalCapacity];
		this.size = 0;
	}

	/**
	 * @param other a non-null reference to some other Collection
	 */
	public ArrayIndexedCollection(Collection other) {
		this(other, DEFAULT_CAPACITY);
	}
	
	/**
	 * @param other a non-null reference to some other Collection
	 * @param initalCapacity
	 * @throws NullPointerException when param other equals null
	 * @throws IllegalArgumentException when param initialCapacity is less than 1
	 */
	public ArrayIndexedCollection(Collection other, int initalCapacity) {
		if (other == null) throw new NullPointerException("Collection other equals null");
		if (initalCapacity < 1) throw new IllegalArgumentException("Initial capacity is less than 1");
		
		this.elements = new Object[(initalCapacity > other.size() ? initalCapacity : other.size())];
		this.addAll(other);
		this.size = other.size();
	}


	/**
	 * Gets the object that is stored in backing array at position index
	 * @param index
	 * @return Returns the object that is stored in backing array at position index
	 * @throws IndexOutOfBoundsException when index is invalid
	 */
	public Object get(int index) {
		if (index < 0 || index > this.size-1) throw new IndexOutOfBoundsException("Invalid index");
		return this.elements[index];
	}
	
	/**
	 * Inserts (does not overwrite) the given value at the given position in array
	 * @param value Inserts the value at the position in array
	 * @param position
	 * @throws NullPointerException when object is null
	 * @throws IndexOutOfBoundsException when index is invalid
	 */
	public void insert(Object value, int position) {
		if (value == null) throw new NullPointerException("Object is null");
		if (position < 0 || position > this.elements.length) throw new IndexOutOfBoundsException("Index is invalid");
		
		if (this.size >= this.elements.length) {
			this.elements = Arrays.copyOf(this.elements, this.elements.length * 2);
		}
		
		Object[] tempArray = this.elements.clone();
		
		for(int i = 0; i < this.elements.length; i++) {
			if (i == position) {
				this.elements[i] = value;
			} else if (i > position) {
				this.elements[i] = tempArray[i-1];
			}
		}
		this.size++;
		this.modificationCount++;
	}
	
	/**
	 * Searches the collection and returns the index of the first occurrence of the given value or -1 if the value is not found
	 * @param value
	 * @return returns the index of the first occurrence of the given value or -1 if the value is not found
	 */
	public int indexOf(Object value) {
		if (value == null) return -1;
		for(int i = 0; i < this.size; i++) {
			if (value.equals(this.elements[i])) return i;
		}
		return -1;
	}
	
	/**
	 * Removes element at specified index from collection
	 * @param index Removes element at index
	 * @throws IndexOutOfBoundsException when index is invalid
	 */
	public void remove(int index) {
		if (index < 0 || index > this.size) throw new IndexOutOfBoundsException("Index is invalid");
		
		for(int i = index; i < this.size-1; i++) {
			this.elements[i] = this.elements[i+1];
			if (i == this.size-1) this.elements[i+1] = null;
		}
		
		this.size--;
		this.modificationCount++;
	}
	
	
	/**
	 *@param
	 *@return 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		
		if (!(obj instanceof ArrayIndexedCollection)) return false;
		
		ArrayIndexedCollection o = (ArrayIndexedCollection) obj;
		
		return Arrays.equals(this.elements, o.elements) && this.size == o.size;
	}
	

	/**
	 *
	 *@throws NullPointerException when value is null
	 */
	@Override
	public void add(Object value) {
		if (value == null) throw new NullPointerException();
		
		if (this.size >= this.elements.length) {
			this.elements = Arrays.copyOf(this.elements, this.elements.length * 2);
		}
		
		this.elements[size] = value;
		this.size++;
		
		this.modificationCount++;
	}
	
	/**
	 *
	 */
	@Override
	public void clear() {
		for (int i = 0; i < this.elements.length; i++) {
			this.elements[i] = null;
		}
		this.modificationCount++;
	}

	/**
	 *
	 */
	@Override
	public int size() {
		return this.size;
	}

	/**
	 *
	 */
	@Override
	public boolean contains(Object value) {
		int index = this.indexOf(value);
		if (index == -1) return false;
		return true;
	}

	/**
	 *
	 */
	@Override
	public boolean remove(Object value) {
		int index = this.indexOf(value);
		
		if (index == -1) return false;
		
		this.remove(index);
		
		this.modificationCount++;
		
		return true;
	}

	/**
	 *
	 */
	@Override
	public Object[] toArray() {
		return Arrays.copyOf(this.elements, this.size);
	}

	/**
	 *
	 */
	@Override
	public ElementsGetter createElementsGetter() {
		return new ArrayIndexedCollectionElementsGetter(this);
	}
	
	/**
	 * Implementation of ElementsGetter
	 *
	 */
	private static class ArrayIndexedCollectionElementsGetter implements ElementsGetter {
		
		private final ArrayIndexedCollection arrayIndexedCollection;
		
		private int nextToGet;
		
		private long savedModificationCount;
		
		private ArrayIndexedCollectionElementsGetter(ArrayIndexedCollection arrayIndexedCollection) {
			if (arrayIndexedCollection == null) throw new NullPointerException("Collection is null");
			this.arrayIndexedCollection = arrayIndexedCollection;
			this.nextToGet = 0;
			this.savedModificationCount = arrayIndexedCollection.modificationCount;
		}
				
		
		@Override
		public boolean hasNextElement() {
			if (this.savedModificationCount != this.arrayIndexedCollection.modificationCount)
				throw new ConcurrentModificationException("Collection modified");
			if (this.nextToGet < this.arrayIndexedCollection.size) 
				return true;
			else
				return false;
		}

		@Override
		public Object getNextElement() {
			if (this.savedModificationCount != this.arrayIndexedCollection.modificationCount)
				throw new ConcurrentModificationException("Collection modified");
			if (!hasNextElement()) throw new NoSuchElementException("No elements left");
			
			return this.arrayIndexedCollection.get(this.nextToGet++);
		}
		
	}
	
	
	
}
