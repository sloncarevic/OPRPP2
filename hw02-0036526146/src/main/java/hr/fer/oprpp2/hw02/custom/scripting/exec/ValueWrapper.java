package hr.fer.oprpp2.hw02.custom.scripting.exec;


/**
 * ValueWrapper
 *
 */
public class ValueWrapper {

	private Object value;
	
	/**
	 * Constructor
	 * @param value
	 */
	public ValueWrapper(Object value) {
		this.value = value;
	}

//	public ValueWrapper(Object value) {
//		if (value == null || value instanceof Integer || value instanceof Double || value instanceof String)
//			this.value = value;
//		else
//			throw new RuntimeException("Value type not supported!");
//	}

	/**
	 * @return Object value
	 */
	public Object getValue() {
		return this.value;
	}

	/**
	 * Set value
	 * @param value
	 */
	public void setValue(Object value) {
		if (value == null || value instanceof Integer || value instanceof Double || value instanceof String)
			this.value = value;
		else
			throw new RuntimeException("Value type not supported!");
	}

	/**Adding
	 * @param incValue
	 */
	public void add(Object incValue) {
		if (areInteger(this.value, incValue))
			this.value = Integer.valueOf( (int) (getValueAsDouble(this.value) + getValueAsDouble(incValue)));
		else 
			this.value = (getValueAsDouble(this.value) + getValueAsDouble(incValue));
	}

	/**Subtracting
	 * @param decValue
	 */
	public void subtract(Object decValue) {
		if (areInteger(this.value, decValue))
			this.value = (int) (getValueAsDouble(this.value) - getValueAsDouble(decValue));
		else
			this.value = (getValueAsDouble(this.value) - getValueAsDouble(decValue));
	}

	/**Multiplying
	 * @param mulValue
	 */
	public void multiply(Object mulValue) {
		if (areInteger(this.value, mulValue))
			this.value = (int) (getValueAsDouble(this.value) * getValueAsDouble(mulValue));
		else
			this.value = (getValueAsDouble(this.value) * getValueAsDouble(mulValue));
	}

	/**Dividing
	 * @param divValue
	 */
	public void divide(Object divValue) {
		if (areInteger(this.value, divValue))
			this.value = (int) (getValueAsDouble(this.value) / getValueAsDouble(divValue));
		else
			this.value = (getValueAsDouble(this.value) / getValueAsDouble(divValue));
	}

	/**
	 * Comparator
	 * @param withValue
	 * @return
	 */
	public int numCompare(Object withValue) {
		return Double.compare(getValueAsDouble(this.value), getValueAsDouble(withValue));
	}
	

	/**
	 * @param value
	 * @return double value of given Object
	 */
	private double getValueAsDouble(Object value) {
		if (value instanceof ValueWrapper)
			value = ((ValueWrapper) value).value;
		
		if (value == null)
			return 0;
		else if (value instanceof Integer)
			return (double) (Integer) value;
		else if (value instanceof Double)
			return (double) value;
		else if (value instanceof String) {
			try {
				if (((String) value).contains(".") || ((String) value).toLowerCase().contains("e")) {
					return Double.parseDouble((String) value);
				} else {
					return Integer.parseInt((String) value);
				}
			} catch (NumberFormatException e) {
				throw new RuntimeException("String value can not be parsed !"  + " Value: "+ value.toString());
			}
		} else
			throw new RuntimeException("Value can not be parsed!" + " ValueClass: "+ ((ValueWrapper)value).getValue().toString());
	}
	
	/**
	 * @param value
	 * @return Returns true if integer else false
	 */
	private boolean isInteger(Object value) {
		if (value == null)
			return true;
		else if (value instanceof Integer)
			return true;
		else if (value instanceof String && !((String)value).toLowerCase().contains("e") && !((String)value).contains("."))
			return true;
		return false;
	}
	
	/**
	 * @param value1
	 * @param value2
	 * @return Returns true if both are integers else false
	 */
	private boolean areInteger(Object value1, Object value2) {
		if (value1 instanceof ValueWrapper)
			value1 = ((ValueWrapper) value1).getValue();
		if (value2 instanceof ValueWrapper)
			value2 = ((ValueWrapper) value2).getValue();
		
		if (isInteger(value1) && isInteger(value2))
			return true;
		return false;
	}
	
	
	@Override
	public String toString() {
		if (this.value == null)
			return "";
		else if (this.value instanceof Integer)
			return String.valueOf(((Integer)this.value).intValue());
		else if (this.value instanceof Double)
            return String.valueOf(((Double) this.value).doubleValue());
		else {
			if (this.value.toString().contains("\""))
				return this.value.toString().replace("\"", "");
			else
				return this.value.toString();
		}
	}

}
