package hr.fer.oprpp2.hw01.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import hr.fer.oprpp2.hw01.messages.AckMessage;
import hr.fer.oprpp2.hw01.messages.ByeMessage;
import hr.fer.oprpp2.hw01.messages.HelloMessage;
import hr.fer.oprpp2.hw01.messages.InMessage;
import hr.fer.oprpp2.hw01.messages.Message;
import hr.fer.oprpp2.hw01.messages.OutMessage;

/**
 * MessageUtil class - implements helper methods for message conversion
 *
 */
public class MessageUtil {

	/**
	 * Transform Message to byte array
	 * @param message
	 * @return byte[]
	 */
	public static byte[] transformMessageToByte(Message message) {

		if (message == null)
			throw new NullPointerException("Message can't be null!");

		try {

			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			DataOutputStream dos = new DataOutputStream(bos);

			dos.writeByte(message.getMessageTypeNumber());

			dos.writeLong(message.getMessageNumber());

			switch (message.getMessageType()) {

			case HELLO:
				dos.writeUTF(((HelloMessage) message).getName());
				dos.writeLong(((HelloMessage) message).getKey());
				dos.close();
				return bos.toByteArray();

			case ACK:
				dos.writeLong(((AckMessage) message).getUid());
				dos.close();
				return bos.toByteArray();

			case BYE:
				dos.writeLong(((ByeMessage) message).getUid());
				dos.close();
				return bos.toByteArray();

			case OUTMSG:
				dos.writeLong(((OutMessage) message).getUid());
				dos.writeUTF(((OutMessage) message).getText());
				dos.close();
				return bos.toByteArray();

			case INMSG:
				dos.writeUTF(((InMessage) message).getName());
				dos.writeUTF(((InMessage) message).getText());
				dos.close();
				return bos.toByteArray();

			}

		} catch (IOException e) {
			System.out.println("Exception while serializing");
			e.printStackTrace();
		}

		return null; // my exception
	}

	/**
	 * Transforms byte array to Message
	 * @param byteArr
	 * @param offset
	 * @param length
	 * @return Message
	 */
	public static Message transformByteToMessage(byte[] byteArr, int offset, int length) {

		try {

			ByteArrayInputStream bis = new ByteArrayInputStream(byteArr, offset, length);
			DataInputStream dis = new DataInputStream(bis);

			switch (dis.readByte()) {
			case 1:
				return new HelloMessage(dis.readLong(), dis.readUTF(), dis.readLong());

			case 2:
				return new AckMessage(dis.readLong(), dis.readLong());

			case 3:
				return new ByeMessage(dis.readLong(), dis.readLong());

			case 4:
				return new OutMessage(dis.readLong(), dis.readLong(), dis.readUTF());

			case 5:
				return new InMessage(dis.readLong(), dis.readUTF(), dis.readUTF());

			}
		} catch (IOException e) {
			System.out.println("Exception while deserializing");
			e.printStackTrace();
		}
		
		return null; // my exception

	}

}
