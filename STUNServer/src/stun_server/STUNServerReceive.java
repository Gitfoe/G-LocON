package stun_server;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

//受信用スタンサーバ
public class STUNServerReceive extends Thread {

	//メンバ変数
	DatagramSocket datagramSocket;
	UserInfo userInfo;

	//コンストラクタ（引数なし）
	public STUNServerReceive() {
		super();
	}

	//コンストラクタ（引数あり）
	public STUNServerReceive(DatagramSocket datagramSocket, UserInfo userInfo) {
		this.datagramSocket = datagramSocket;
		this.userInfo = userInfo;
	}


	/**
	 * 非同期処理
	 */
	public void run() {
		if (datagramSocket == null) {
			System.out.println("socket is null");
		}

		while (true) {
			//データ受信
			DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
			try {
				datagramSocket.receive(receivePacket);
				String getMsg = new String(receivePacket.getData(), 0, receivePacket.getLength());
				if (getMsg.equals("Hello")) {

					onReceiveMsg(receivePacket); //userInfoに格納

				} else {
					System.out.println("STUNServer;getMsg:" + getMsg);
				}
			} catch (IOException e) {
				System.out.println("受信失敗" + e.getMessage());
			}
		}
	}


	/**
	 * メッセージ受信
	 * @param datagramPacket
	 */
	public void onReceiveMsg(DatagramPacket datagramPacket) {
		InetAddress IPAddress = datagramPacket.getAddress(); //アドレス取得
		int port = datagramPacket.getPort(); //ポート番号取得

		userInfo.setIPAddress(IPAddress);
		userInfo.setPort(port);

		//userInfo表示
		userInfo.printInfo();

		onSendeMsg();
	}


	/*
	 * ServerSend生成
	 */
	public void onSendeMsg() {

		//送信用スタンサーバ生成
		STUNServerSend stunServerSend = new STUNServerSend(datagramSocket, userInfo);
		stunServerSend.start();
	}

}
