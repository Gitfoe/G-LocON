package signaling_server;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class StartUp {
	private final static int myPort = 55555; // Port number

	public static void main(String[] args) {

		ArrayList<UserInfo> userInfoList = new ArrayList<>();
		DatagramSocket socket;

		try {
			socket = new DatagramSocket(myPort);

			// Start of asynchronous processing (signaling server)
			SignalingServerReceive receive = new SignalingServerReceive(socket, userInfoList);
			receive.start();
			System.out.println("The Signaling server started on port " + myPort);

		} catch (SocketException e) {
			System.out.println("Error socket");
			e.printStackTrace();
		}
	}
}