package stun_server;

import java.net.InetAddress;

//ユーザのIPアドレスとポート番号
public class UserInfo {
	private InetAddress IPAddress;
	private int port;

	//コンストラクタ（引数なし）
	public UserInfo() {
	}

	//コンストラクタ（引数あり）
	public UserInfo(InetAddress IPAddress, int port) {
		this.IPAddress = IPAddress;
		this.port = port;
	}

	/**
	 * @return iPAddress
	 */
	public InetAddress getIPAddress() {
		return IPAddress;
	}

	/**
	 * @param iPAddress セットする iPAddress
	 */
	public void setIPAddress(InetAddress iPAddress) {
		IPAddress = iPAddress;
	}

	/**
	 * @return port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port セットする port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	public void printInfo() {
		System.out.println("IP>> " + IPAddress + "   Port>>" + port);
	}

}
