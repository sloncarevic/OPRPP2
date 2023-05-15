package hr.fer.oprpp2.hw01.messages;

/**
 * Class ByeMessage extended from Message - model of Bye message
 *
 */
public class ByeMessage extends Message {
	
	private long uid;
	
	/**
	 * Constructor
	 * @param messageNumber
	 * @param uid
	 */
	public ByeMessage(long messageNumber, long uid) {
		super(messageNumber, MessageType.BYE);
		this.uid = uid;
	}

	/**
	 * Getter uid
	 * @return uid
	 */
	public long getUid() {
		return uid;
	}
	
	@Override
	public String toString() {
		return "BYE(" + getMessageNumber() + ", uid= " + this.uid + ")";
	}

}
