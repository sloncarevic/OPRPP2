package hr.fer.oprpp2.hw01.messages;

/**
 * Abstract class Message - model of message
 *
 */
public abstract class Message {

	private long messageNumber;

	private MessageType messageType;

	/**
	 * Default constructor
	 * @param messageNumber
	 * @param messageType
	 */
	public Message(long messageNumber, MessageType messageType) {
		this.messageNumber = messageNumber;
		this.messageType = messageType;
	}

	/**
	 * @return Returns message type
	 */
	public MessageType getMessageType() {
		return messageType;
	}

	/**
	 * @return Returns message type by number
	 */
	public int getMessageTypeNumber() {
		switch (this.messageType) {
		case HELLO:
			return 1;
		case ACK:
			return 2;
		case BYE:
			return 3;
		case OUTMSG:
			return 4;
		case INMSG:
			return 5;
		}
		
		return 0;
	}

	/**
	 * @return Returns message number
	 */
	public long getMessageNumber() {
		return messageNumber;
	}

}
