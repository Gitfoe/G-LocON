package stun_server.Model;

import java.net.InetAddress;

public class UserInfo {
	// User's IP address and port number
	private InetAddress IPAddress;
	private int port;

	public UserInfo() {
	}

	public UserInfo(InetAddress IPAddress, int port) {
		this.IPAddress = IPAddress;
		this.port = port;
	}

	/**
	 * @return ipAddress
	 */
	public InetAddress getIPAddress() {
		return IPAddress;
	}

	/**
	 * @param ipAddress Set ipAddress
	 */
	public void setIPAddress(InetAddress ipAddress) {
		IPAddress = ipAddress;
	}

	/**
	 * @return port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port Set port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	public void printInfo() {
		System.out.println("IP: " + IPAddress + ", Port: " + port);
	}
}