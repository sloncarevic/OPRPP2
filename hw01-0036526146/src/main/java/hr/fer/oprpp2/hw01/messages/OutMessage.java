package hr.fer.oprpp2.hw01.messages;

/**
 * Class OutMessage extended from Message - model of Out message
 *
 */
public class OutMessage extends Message {
	 
	private long uid;
	
	private String text;
	
	/**
	 * Constructor
	 * @param messageNumber
	 * @param uid
	 * @param text
	 */
	public OutMessage(long messageNumber, long uid, String text) {
		super(messageNumber, MessageType.OUTMSG);
		this.uid = uid;
		this.text = text;
	}

	/** Getter uid
	 * @return uid
	 */
	public long getUid() {
		return uid;
	}

	/** Getter text
	 * @return text
	 */
	public String getText() {
		return text;
	}
	
	@Override
	public String toString() {
		return "OUTMESSAGE(" + getMessageNumber() + ", uid=" + this.uid + ", text=" + this.text + ")";
	}

}
