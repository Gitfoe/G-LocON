package stun_server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

//送信用スタンサーバー
public class STUNServerSend extends Thread {

	//メンバ変数
	private DatagramSocket datagramSocket;
	private UserInfo userInfo;

	//コンストラク（引数なし）
	public STUNServerSend() {
		super();
	}

	//コンストラクタ（引数あり）
	public STUNServerSend(DatagramSocket datagramSocket, UserInfo userInfo) {
		this.datagramSocket = datagramSocket;
		this.userInfo = userInfo;
	}


	/**
	 * 非同期処理
	 */
	public void run() {
		String addrPort = userInfo.getIPAddress().getHostAddress().toString() + "-"
				+ String.valueOf(userInfo.getPort());
		try {
			datagramSocket.send(new DatagramPacket(addrPort.getBytes(),
					addrPort.getBytes().length, userInfo.getIPAddress(), userInfo.getPort()));
			System.out.println("送信元端末への送信完了");
		} catch (IOException e) {
			System.out.println("送信失敗：" + e.getMessage());
		}

	}

}