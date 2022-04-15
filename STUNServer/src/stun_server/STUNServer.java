package stun_server;


import java.net.DatagramSocket;

//
public class STUNServer extends Thread {

	//メンバ変数
	private DatagramSocket serverSocket;
	private UserInfo userInfo;

	//コンストラクタ（引数なし）
	public STUNServer() {
		super();
	}

	//コンストラクタ（引数あり）
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
	 * @param serverSocket セットする serverSocket
	 */
	public void setServerSocket(DatagramSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	/**
	 * 非同期処理
	 */
	public void run() {
		STUNServerReceive stunServerReceive = new STUNServerReceive(serverSocket, userInfo);
		stunServerReceive.start();
	}

}

