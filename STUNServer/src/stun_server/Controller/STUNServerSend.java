package stun_server.Controller;

import stun_server.Model.UserInfo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

// Outgoing STUN server
public class STUNServerSend extends Thread {

	private DatagramSocket datagramSocket;
	private UserInfo userInfo;

	/**
	 * Constructor (no arguments)
	 */
	public STUNServerSend() {
		super();
	}

	/**
	 * Constructor (with arguments)
	 */
	public STUNServerSend(DatagramSocket datagramSocket, UserInfo userInfo) {
		this.datagramSocket = datagramSocket;
		this.userInfo = userInfo;
	}

	/**
	 * Asynchronous processing
	 */
	public void run() {
		String addrPort = userInfo.getIPAddress().getHostAddress().toString() + "-"
				+ String.valueOf(userInfo.getPort());
		try {
			datagramSocket.send(new DatagramPacket(addrPort.getBytes(),
					addrPort.getBytes().length, userInfo.getIPAddress(), userInfo.getPort()));
			System.out.println("Completion of transmission to the source terminal");
		} catch (IOException e) {
			System.out.println("Transmission failure： " + e.getMessage());
		}
	}
}