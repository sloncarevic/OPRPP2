package hr.fer.oprpp2.hw01.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.oprpp2.hw01.messages.AckMessage;
import hr.fer.oprpp2.hw01.messages.ByeMessage;
import hr.fer.oprpp2.hw01.messages.HelloMessage;
import hr.fer.oprpp2.hw01.messages.InMessage;
import hr.fer.oprpp2.hw01.messages.Message;
import hr.fer.oprpp2.hw01.messages.MessageType;
import hr.fer.oprpp2.hw01.messages.OutMessage;
import hr.fer.oprpp2.hw01.util.MessageUtil;

/**
 * Client class - model of GUI application - client for UDP server
 *
 */
public class Client extends JFrame {

	private static final long serialVersionUID = 1L;

	private InetAddress inetAddress;

	private int port;

	private DatagramSocket datagramSocket;

	private long uid;

	private String name;

	private long clientMessageNumber = 0L;

	private long serverMessageNumber = 0L;

	private JTextField textField;

	private JTextArea textArea;

	private BlockingQueue<Message> queueAck = new LinkedBlockingQueue<Message>();

	//private BlockingQueue<Message> queueToSend = new LinkedBlockingQueue<Message>();

	/**
	 * Constructor
	 * @param inetAddress
	 * @param port
	 * @param datagramSocket
	 * @param uid
	 * @param name
	 */
	public Client(InetAddress inetAddress, int port, DatagramSocket datagramSocket, long uid, String name) {
		this.inetAddress = inetAddress;
		this.port = port;
		this.datagramSocket = datagramSocket;
		this.uid = uid;
		this.name = name;

		initGUI();

	}

	/**
	 * GUI initialisation
	 */
	private void initGUI() {

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeConnection();
				dispose();
			}
		});

		setLocation(20, 20);
		setSize(600, 300);
		setTitle("Chat client: " + this.name);

		this.getContentPane().setLayout(new BorderLayout());

		this.textField = new JTextField();
		this.getContentPane().add(this.textField, BorderLayout.NORTH);

		this.textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea);
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);


		this.textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = textField.getText().strip();
				textField.setText("");
				textField.setEnabled(false);

				(new Thread(() -> {
					int trys = 0;
					while (trys < 10) {
						trys++;
						//System.out.println("trys: " + trys);

						OutMessage outMessage = new OutMessage(clientMessageNumber + 1, uid, text);

						byte[] byteArr = MessageUtil.transformMessageToByte(outMessage);

						DatagramPacket datagramPacket = new DatagramPacket(byteArr, byteArr.length, inetAddress, port);

						try {
							datagramSocket.send(datagramPacket);
							
						} catch (IOException ioe) {
							System.out.println("Exception while sending");
							ioe.printStackTrace();
						}

						try {

							Message msg = queueAck.poll(1000, TimeUnit.MILLISECONDS); //5000
							
							if (msg == null) {
								if (trys >= 10) {
									System.out.println("Can't send message. Timeout 10 times");
									textField.setEnabled(true);
									return;
								}
								continue;
							}
							
							if (msg.getMessageNumber() == clientMessageNumber + 1) {
								// success
								clientMessageNumber++;
								break;
							} else {
								System.out.println("invalid number");
								//queue.put(msg);
								//continue;
							}
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}

					}

					textField.setEnabled(true);
					textField.requestFocus();
				})).start();
			}
		});

		(new Thread(() -> {
			receiving();
		})).start();

	}

	private void receiving() {

		while (!this.datagramSocket.isClosed()) {

			byte[] byteArrRecv = new byte[1500];
			DatagramPacket datagramPacketRecv = new DatagramPacket(byteArrRecv, byteArrRecv.length);

			try {
				this.datagramSocket.setSoTimeout(0);
				this.datagramSocket.receive(datagramPacketRecv);
			} catch (SocketTimeoutException ste) {
				continue;
			} catch (SocketException e) {
				continue;
				// return;
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}

			Message message = MessageUtil.transformByteToMessage(datagramPacketRecv.getData(),
					datagramPacketRecv.getOffset(), datagramPacketRecv.getLength());

			if (message.getMessageType() == MessageType.ACK) {
				while (true) {
					try {
						queueAck.put(message);
						//this.serverMessageNumber++;
						break;
					} catch (InterruptedException e) {
						// e.printStackTrace();
					}
				}
				continue;
			} else if (message.getMessageType() == MessageType.INMSG) {

				if (message.getMessageNumber() == this.serverMessageNumber + 1) {

					this.serverMessageNumber++;
					//System.out.println("messageNumber: " + this.serverMessageNumber);

					this.textArea.append("[" + datagramPacketRecv.getSocketAddress() + "]" + " Poruka od korisnika: "
							+ ((InMessage) message).getName() + "\n" + ((InMessage) message).getText() + "\n\n");

					
					sendAck(message.getMessageNumber(), this.uid);

				}else if (message.getMessageNumber() == this.serverMessageNumber) {
					//this.serverMessageNumber--;
					sendAck(message.getMessageNumber(), this.uid);
					
				}
				else {
					System.out.println("Invalid server message number");
				}
			} else {
				System.out.println("Invalid message (type) received");
			}

		}
	}
	
	private void sendAck(long msgNum, long uid) {
		AckMessage ackMessage = new AckMessage(msgNum, uid);

		byte[] byteArrSend = MessageUtil.transformMessageToByte(ackMessage);

		DatagramPacket datagramPacketSend = new DatagramPacket(byteArrSend, byteArrSend.length, this.inetAddress, this.port);

		try {
			this.datagramSocket.send(datagramPacketSend);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private static void startConnection(InetAddress inetAddressArgs, int portArg, String nameArg) throws IOException {

		@SuppressWarnings("resource")
		DatagramSocket datagramSocket = new DatagramSocket();

		Random random = new Random();
		HelloMessage helloMessage = new HelloMessage(0L, nameArg, random.nextLong());

		int trys = 0;

		boolean ansReceived = false;

		long uid = 0L;

		while (trys < 10) {
			trys++;
			ansReceived = false;

			byte[] byteArr = MessageUtil.transformMessageToByte(helloMessage);

			DatagramPacket datagramPacketSend = new DatagramPacket(byteArr, byteArr.length);
			datagramPacketSend.setAddress(inetAddressArgs);
			datagramPacketSend.setPort(portArg);

			datagramSocket.send(datagramPacketSend);

			byte[] byteArrRecv = new byte[1500];
			DatagramPacket datagramPacketReceive = new DatagramPacket(byteArrRecv, byteArrRecv.length);

			datagramSocket.setSoTimeout(1000); // 5000

			try {
				datagramSocket.receive(datagramPacketReceive);
			} catch (SocketTimeoutException e) {
				System.out.println("Timeout " + trys);
				
				if (trys >= 10 && !ansReceived) {
					System.out.println("Can't connect");
					return;
				}
				
				continue;
			} catch (IOException e) {
				e.printStackTrace();
			}

			Message message = MessageUtil.transformByteToMessage(datagramPacketReceive.getData(),
					datagramPacketReceive.getOffset(), datagramPacketReceive.getLength());

			if (message.getMessageType() == MessageType.ACK) {
				ansReceived = true;
				uid = ((AckMessage) message).getUid();
				break;
			}
		}

		System.out.println("Veza uspostavljena. Dobili smo uid: " + uid);
		long i = uid;

		SwingUtilities
				.invokeLater(() -> new Client(inetAddressArgs, portArg, datagramSocket, i, nameArg).setVisible(true));

	}

	private void closeConnection() {
		ByeMessage byeMessage = new ByeMessage(++this.clientMessageNumber, this.uid);
		byte[] byteArr = MessageUtil.transformMessageToByte(byeMessage);

		DatagramPacket datagramPacket = new DatagramPacket(byteArr, byteArr.length, this.inetAddress, this.port);

		try {
			this.datagramSocket.send(datagramPacket);
		} catch (IOException e) {
			System.out.println("Exception while sending");
			e.printStackTrace();
		}

		this.datagramSocket.close();

	}

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("Očekivao sam IP port ime");
			return;
		}

		InetAddress inetAddressArgs = null;
		try {
			inetAddressArgs = InetAddress.getByName(args[0]);
		} catch (UnknownHostException e1) {
			
			e1.printStackTrace();
			return;
		}

		int portArg = Integer.parseInt(args[1]);

		String nameArg = args[2].strip();

		try {
			startConnection(inetAddressArgs, portArg, nameArg);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
