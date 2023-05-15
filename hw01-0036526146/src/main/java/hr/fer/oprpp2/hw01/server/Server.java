package hr.fer.oprpp2.hw01.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import hr.fer.oprpp2.hw01.messages.AckMessage;
import hr.fer.oprpp2.hw01.messages.ByeMessage;
import hr.fer.oprpp2.hw01.messages.HelloMessage;
import hr.fer.oprpp2.hw01.messages.InMessage;
import hr.fer.oprpp2.hw01.messages.Message;
import hr.fer.oprpp2.hw01.messages.OutMessage;
import hr.fer.oprpp2.hw01.util.MessageUtil;

/**
 * Server class - model of UDP server
 *
 */
public class Server {

	private DatagramSocket datagramSocket;

	private int port;
	
	private List<ConnectedClient> connectedClients = Collections.synchronizedList(new ArrayList<>());
	
	//user id to be assignet to connected users
	private Long uid;

	/**
	 * Constructor
	 * @param port
	 */
	public Server(int port) {

		this.port = port;

		try {
			datagramSocket = new DatagramSocket(null);
			datagramSocket.bind(new InetSocketAddress((InetAddress) null, this.port));
		} catch (SocketException e) {
			System.out.println("Can't create datagram socket");
			e.printStackTrace();
			return;
		}
		
		Random random = new Random();
		
		this.uid = random.nextLong();

		server();

	}

	/**
	 * server method - infinite loop receiving UDP datagram packets
	 */
	private void server() {

		while (true) {

			byte[] byteArrRecv = new byte[1500];
			DatagramPacket datagramPacket = new DatagramPacket(byteArrRecv, byteArrRecv.length);

			try {
				this.datagramSocket.receive(datagramPacket);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}

			System.out.println("Primljen paket od: " + datagramPacket.getSocketAddress());

			Message message = MessageUtil.transformByteToMessage(datagramPacket.getData(), datagramPacket.getOffset(),
					datagramPacket.getLength());
			
			if (message != null) {
				System.out.println("Paket je: " + message.getMessageType().toString());

				processMessage(datagramPacket, message);				
			}
			else 
				System.out.println("Neuspjesno citanje poruke!");


		}
	}

	/**
	 * Processing Message by type
	 * @param datagramPacket
	 * @param message
	 */
	private void processMessage(DatagramPacket datagramPacket, Message message) {
		if (message == null) {
			System.out.println("Message is null");
			return;
		}

		switch (message.getMessageType()) {
		
		case HELLO:
			processHelloMessage(datagramPacket, (HelloMessage)message);
			break;
			
		case BYE:
			processByeMessage((ByeMessage) message);
			break;

		case ACK:
			processAckMessage((AckMessage) message);
			break;
			
		case OUTMSG:
			processOutMessage((OutMessage)message);
			break;
			
		case INMSG:
			//processInMessage();
			System.out.println("INMSG - krivi tip poruke");
			break;
		}

	}

	/**
	 * Processing Hello message
	 * @param datagramPacket
	 * @param message
	 */
	private void processHelloMessage(DatagramPacket datagramPacket, HelloMessage message) {
		ConnectedClient connectedClient = null;
		synchronized (this.connectedClients) {
			for (var cc : this.connectedClients) {
				if (cc.getKey() == message.getKey()) {
					connectedClient = cc;
				}
			}
			if (connectedClient == null) {
				connectedClient = new ConnectedClient(
						message.getKey(), message.getName(), ++uid, datagramPacket.getAddress(), datagramPacket.getPort());
				this.connectedClients.add(connectedClient);
				
				ConnectedClient client = connectedClient; //ef
				(new Thread(() -> {
					connectionThread(client);
				})).start();
					
			}
			sendAck(connectedClient);
			
		}

	}
	
	/**
	 * Processing Bye message
	 * @param byeMessage
	 */
	private void processByeMessage(ByeMessage byeMessage) {
		ConnectedClient cc = findConnectedClientByUid(byeMessage.getUid());
		
		if (cc == null)
			return;
		
		sendAck(cc);
		
		cc.setConnected(false);
		
		this.connectedClients.remove(cc);
		
	}

	/**
	 * Processing Ack message
	 * @param ackMessage
	 */
	private void processAckMessage(AckMessage ackMessage) {
		ConnectedClient cc = findConnectedClientByUid(ackMessage.getUid());
		if (cc == null)
			return;
		
		try {
			cc.getQueueRecv().put(ackMessage);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		

	}

	/**
	 * Processing Out message
	 * @param outMessage
	 */
	private void processOutMessage(OutMessage outMessage) {
		ConnectedClient connectedClient = findConnectedClientByUid(outMessage.getUid());
		if (connectedClient == null)
			return;
		
		sendAck(connectedClient);
		
		String name = connectedClient.getName();
		synchronized (this.connectedClients) {
			for (var cc : this.connectedClients) {
				InMessage message = new InMessage(++cc.serverMessageNumber, name, outMessage.getText());
				try {
					cc.getQueueSend().put(message);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

//	private void processInMessage() {
//		//nista
//	}
	
	/**
	 * Helper method - finds connected client by uid
	 * @param uid
	 * @return ConnectedClient
	 */
	private ConnectedClient findConnectedClientByUid (long uid) {
		for (var cc : this.connectedClients) {
			if (cc.getUid() == uid)
				return cc;
		}
		return null;
	}
	
	/**
	 * Send Ack message
	 * @param client
	 */
	private void sendAck(ConnectedClient client) {
		AckMessage ackMessage = new AckMessage(client.clientMessageNumber++, client.getUid());
		byte[] byteArrSend = MessageUtil.transformMessageToByte(ackMessage);
		DatagramPacket datagramPacket = new DatagramPacket(
				byteArrSend, byteArrSend.length, client.getInetAddress(), client.getPort());
		
		try {
			this.datagramSocket.send(datagramPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Client thread job
	 * @param connectedClient
	 */
	private void connectionThread(ConnectedClient connectedClient) {
		while (connectedClient.isConnected()) {
			try {
				Message message = connectedClient.getQueueSend().take();
				sendMessage(connectedClient, message);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Send message
	 * @param connectedClient
	 * @param message
	 */
	private void sendMessage(ConnectedClient connectedClient, Message message) {
		byte[] byteArrSend = MessageUtil.transformMessageToByte(message);
		DatagramPacket datagramPacket = new DatagramPacket(
				byteArrSend, byteArrSend.length, connectedClient.getInetAddress(), connectedClient.getPort());
		
		int trys = 0;
		while (trys < 10) {
			trys++;
			
			try {
				this.datagramSocket.send(datagramPacket);
			} catch (IOException e) {
				System.out.println("Exception while sending");
				e.printStackTrace();
			}
			
			try {
				Message messageRecv = connectedClient.getQueueRecv().poll(1000, TimeUnit.MILLISECONDS);
				
				if (messageRecv == null) {
					if (trys >= 10) {
						System.out.println("Can't send message. Timeout 10 times");
						return;
					}
					continue;
				}
				
				if (messageRecv.getMessageNumber() == message.getMessageNumber()) {
					//success
					break;
				} else {
					System.out.println("Invalid number");
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}

	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Očekivao sam port");
			return;
		}
		
		new Server(Integer.parseInt(args[0].strip()));
		
		System.out.println("Closing");

	}

}
