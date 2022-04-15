package stun_server;

import java.net.DatagramSocket;
import java.net.SocketException;

public class StartUp {

	final static int SERVER_PORT = 55554; //ポート番号

	public static void main(String[] args) {

		//オブジェクト生成
		DatagramSocket serverSocket;
		UserInfo userInfo = new UserInfo();

		try {
			//ソケット生成
			serverSocket = new DatagramSocket(SERVER_PORT);

			//スタンサーバのオブジェクト生成
			STUNServer stunServer = new STUNServer(serverSocket, userInfo);

			//非同期処理
			stunServer.start();

			System.out.println("STUNサーバは，ポート番号" + SERVER_PORT + "で起動");

		} catch (SocketException e) {
			System.out.println(e.getMessage());
		}

	}

}