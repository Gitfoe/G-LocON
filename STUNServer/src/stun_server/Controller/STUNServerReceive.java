package stun_server.Controller;


import stun_server.Model.UserInfo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

// Receiving STUN server
public class STUNServerReceive extends Thread {

	private DatagramSocket datagramSocket;
	private UserInfo userInfo;

	public STUNServerReceive() {
		super();
	}

	public STUNServerReceive(DatagramSocket datagramSocket, UserInfo userInfo) {
		this.datagramSocket = datagramSocket;
		this.userInfo = userInfo;
	}

	/**
	 * Asynchronous processing
	 */
	public void run() {
		if (datagramSocket == null) {
			System.out.println("Socket is null");
		}

		while (true) {
			// Data reception
			DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
			try {
				datagramSocket.receive(receivePacket);
				String getMsg = new String(receivePacket.getData(), 0, receivePacket.getLength());
				if (getMsg.equals("Hello")) {
					onReceiveMsg(receivePacket); // Stored in userInfo
				} else {
					System.out.println("STUNServer;getMsg:" + getMsg);
				}
			} catch (IOException e) {
				System.out.println("Receiving failure: " + e.getMessage());
			}
		}
	}

	/**
	 * Message received
	 */
	public void onReceiveMsg(DatagramPacket datagramPacket) {
		InetAddress IPAddress = datagramPacket.getAddress(); // IP address acquisition
		int port = datagramPacket.getPort(); // Port number acquisition

		userInfo.setIPAddress(IPAddress);
		userInfo.setPort(port);

		// Display userInfo
		userInfo.printInfo();

		onSendMsg();
	}

	/*
	 * ServerSend generation
	 */
	public void onSendMsg() {
		// STUN server generation for transmission
		STUNServerSend stunServerSend = new STUNServerSend(datagramSocket, userInfo);
		stunServerSend.start();
	}
}