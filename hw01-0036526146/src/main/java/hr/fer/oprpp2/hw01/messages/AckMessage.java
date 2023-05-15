package hr.fer.oprpp2.hw01.messages;

/**
 * Class AckMessage extended from Message - model of Ack message
 *
 */
public class AckMessage extends Message {
	
	private long uid;

	/**
	 * Constructor
	 * @param messageNumber
	 * @param uid
	 */
	public AckMessage(long messageNumber, long uid) {
		super(messageNumber, MessageType.ACK);
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
		return "ACK(" + getMessageNumber() + ", uid= " + this.uid + ")";
	}

}

