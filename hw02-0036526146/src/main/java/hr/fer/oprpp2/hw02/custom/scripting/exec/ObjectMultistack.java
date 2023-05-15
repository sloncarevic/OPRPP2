package hr.fer.oprpp2.hw02.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;

/**
 * ObjectMultistack allows the user to store multiple values for same key and it provides a stack-like abstraction
 *
 */
public class ObjectMultistack {
	
	
	/**
	 *  MultistackEntry acts as a node of a single-linked list
	 *
	 */
	private static class MultistackEntry {
		
		private ValueWrapper valueWrapper;
		
		private MultistackEntry next;
		
		public MultistackEntry(ValueWrapper valueWrapper, MultistackEntry next) {
			if (valueWrapper == null) 
				throw new NullPointerException("ValueWrapper can't be null!");
			this.valueWrapper = valueWrapper;
			this.next = next;
		}
		
	}
	
	
	private Map<String, MultistackEntry> multiStack = new HashMap<>();
	
	
	/**
	 * @param keyName
	 * @param valueWrapper
	 */
	public void push(String keyName, ValueWrapper valueWrapper) {
		MultistackEntry entry = multiStack.get(keyName);
		multiStack.put(keyName, new MultistackEntry(valueWrapper, entry));
	}
	
	/**
	 * @param keyName
	 * @return Returns (removes) ValueWrapper from stack
	 */
	public ValueWrapper pop(String keyName) {
		MultistackEntry entry = multiStack.get(keyName);
		multiStack.remove(keyName);
		multiStack.put(keyName, entry.next);
		return entry.valueWrapper;
	}
	
	/**
	 * @param keyName
	 * @return Returns ValueWrapper from stack
	 */
	public ValueWrapper peek(String keyName) {
		MultistackEntry multistackEntry = multiStack.get(keyName);
		return multistackEntry.valueWrapper;
	}
	
	/**
	 * @param keyName
	 * @return Returns true if empty else false
	 */
	public boolean isEmpty(String keyName) {
		return multiStack.get(keyName) == null;
	}

}
