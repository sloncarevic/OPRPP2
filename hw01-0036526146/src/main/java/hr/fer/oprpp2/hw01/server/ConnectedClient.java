package hr.fer.oprpp2.hw01.server;

import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import hr.fer.oprpp2.hw01.messages.Message;

/**
 * ConnectedClient class - model of client connected to udp server
 *
 */
public class ConnectedClient {

	private long key;
	
	private String name;
	
	private long uid;
	
	private InetAddress inetAddress;
	
	private int port;
	
	private BlockingQueue<Message> queueRecv = new LinkedBlockingQueue<Message>();
	
	private BlockingQueue<Message> queueSend = new LinkedBlockingQueue<Message>();
	
	private boolean connected = false;
	

	public long clientMessageNumber = 0L;

	public long serverMessageNumber = 0L;
	

	/**
	 * Constructor
	 * @param key
	 * @param name
	 * @param uid
	 * @param inetAddress
	 * @param port
	 */
	public ConnectedClient(long key, String name, long uid, InetAddress inetAddress, int port) {
		this.key = key;
		this.name = name;
		this.uid = uid;
		this.inetAddress = inetAddress;
		this.port = port;
		
		connected = true;
	}

	/**
	 * @return Returns key
	 */
	public long getKey() {
		return key;
	}

	/**
	 * @return Returns name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return Returns uid
	 */
	public long getUid() {
		return uid;
	}

	/**
	 * @return Returns inetAddress
	 */
	public InetAddress getInetAddress() {
		return inetAddress;
	}

	/**
	 * @return Returns port
	 */
	public int getPort() {
		return port;
	}
	

	/**
	 * @return Returns queueRecv
	 */
	public BlockingQueue<Message> getQueueRecv() {
		return queueRecv;
	}

	/**
	 * @return Returns queueSend
	 */
	public BlockingQueue<Message> getQueueSend() {
		return queueSend;
	}

	/**
	 * @return Returns true if connected, false otherwise
	 */
	public boolean isConnected() {
		return connected;
	}

	
	/**
	 * Set connected 
	 * @param connected boolean
	 */
	public void setConnected(boolean connected) {
		this.connected = connected;
	}


}
