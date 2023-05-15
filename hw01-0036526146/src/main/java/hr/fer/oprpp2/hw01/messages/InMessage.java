package hr.fer.oprpp2.hw01.messages;

/**
 * Class InMessage extended from Message - model of In message
 *
 */
public class InMessage extends Message{
	
	private String name;
	
	private String text;
	
	/**
	 * Constructor
	 * @param messageNumber
	 * @param name
	 * @param text
	 */
	public InMessage(long messageNumber, String name, String text) {
		super(messageNumber, MessageType.INMSG);
		this.name = name;
		this.text = text;
	}

	/**Getter name
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**Getter text
	 * @return text
	 */
	public String getText() {
		return text;
	}
	
	@Override
	public String toString() {
		return "INMESSAGE(" + getMessageNumber() + ", name=" + this.name + ", text=" + this.text + ")";
	}

}
