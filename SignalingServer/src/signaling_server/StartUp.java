package signaling_server;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class StartUp {
	private final static int myPort = 55555; //ポート番号

	public static void main(String[] args) {

		ArrayList<UserInfo> userInfoList = new ArrayList<>();
		DatagramSocket socket;

		try {
			socket = new DatagramSocket(myPort);

			//非同期処理開始（シグナリングサーバ）
			SignalingServerReceive receive = new SignalingServerReceive(socket, userInfoList);
			receive.start();
			System.out.println("Signalingサーバは，ポート番号" + myPort + "で起動．");

		} catch (SocketException e) {
			System.out.println("error socket");
			e.printStackTrace();
		}

	}

}
