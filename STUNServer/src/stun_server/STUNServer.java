package stun_server;

import java.net.DatagramSocket;

public class STUNServer extends Thread {

	private DatagramSocket serverSocket;
	private UserInfo userInfo;

	public STUNServer() {
		super();
	}

	public STUNServer(DatagramSocket serverSocket, UserInfo userInfo) {
		this.serverSocket = serverSocket;
		this.userInfo = userInfo;
	}

	/**
	 * @return serverSocket
	 */
	public DatagramSocket getServerSocket() {
		return serverSocket;
	}

	/**
	 * @param serverSocket Set serverSocket
	 */
	public void setServerSocket(DatagramSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	/**
	 * Asynchronous processing, starts receiving STUN requests
	 */
	public void run() {
		STUNServerReceive stunServerReceive = new STUNServerReceive(serverSocket, userInfo);
		stunServerReceive.start();
	}
}