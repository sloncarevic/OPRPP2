package hr.fer.oprpp2.hw01.messages;

/**
 * Class HelloMessage extended from Message - model of Hello message
 *
 */
public class HelloMessage extends Message{
	
	private String name;
	
	private long key;

	/**
	 * Constructor
	 * @param messageNumber
	 * @param name
	 * @param key
	 */
	public HelloMessage(long messageNumber, String name, long key) {
		super(messageNumber, MessageType.HELLO);
		this.name = name;
		this.key = key;
	}

	/**Getter name
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**Getter key
	 * @return key
	 */
	public long getKey() {
		return key;
	}

	@Override
	public String toString() {
		return "HELLO(" + getMessageNumber() + ", " + this.name + ", " + this.key + ")";
	}
}
