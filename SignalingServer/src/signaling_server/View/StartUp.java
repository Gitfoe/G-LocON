package signaling_server.View;

import signaling_server.Model.UserInfo;
import signaling_server.Controller.SignalingServerReceive;

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
// Used for determining the speed of the anonymization method
//		ArrayList<UserInfo> listForSpeedTest = new ArrayList<UserInfo>();
//		for (int i = 0; i < 1000000; i++) {
//			listForSpeedTest.add(new UserInfo("127.0.0.1", 1337, "127.0.0.1", 1337, 72.0, 50.0, "1"));
//		}
//
//		LocalDateTime startedTime = LocalDateTime.now();
//		System.out.println("Test started: " + startedTime);
//		for (int i = 0; i < listForSpeedTest.size(); i++) {
//			StartUp.anonymizeUser(listForSpeedTest.get(i));
//		}
//		LocalDateTime finishedTime = LocalDateTime.now();
//		System.out.println("Test finished: " + finishedTime);
//		System.out.println("Total elapsed milliseconds: " + ChronoUnit.MILLIS.between(startedTime, finishedTime));
//
//	}
//
//	public static UserInfo anonymizeUser(UserInfo userInfo) {
//		// Generate random string of 40 characters to be used as a peer ID
//		SecureRandom randomGenerator = new SecureRandom();
//		byte[] randomBytes = new byte[20];
//		randomGenerator.nextBytes(randomBytes);
//		String randomString = new BigInteger(1, randomBytes).toString(16);
//		// Create copy of userInfo with anonymized peerID
//		return new UserInfo(userInfo.getPublicIP(), userInfo.getPublicPort(), userInfo.getPrivateIP(),
//				userInfo.getPrivatePort(), userInfo.getLatitude(), userInfo.getLongitude(), randomString);
//	}
}