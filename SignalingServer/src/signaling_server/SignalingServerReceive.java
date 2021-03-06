package signaling_server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

import org.json.JSONObject;

public class SignalingServerReceive extends Thread {
	private DatagramSocket socket;
	private ArrayList<UserInfo> userInfoList;

	public SignalingServerReceive() {
		super();
	}

	public SignalingServerReceive(DatagramSocket socket, ArrayList<UserInfo> userInfoList) {
		this.socket = socket;
		this.userInfoList = userInfoList;
	}

	@Override
	public void run() {
		// Definition of incoming messages
		final String REGISTER = "REGISTER";
		final String UPDATE = "UPDATE";
		final String SEARCH = "SEARCH";
		final String DELETE = "DELETE";

		while (true) {
			// Receive data
			DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
			try {
				socket.receive(receivePacket);

				String result = new String(receivePacket.getData(), 0, receivePacket.getLength());

				JSONObject jsonObject = new JSONObject(result);
				ProcessJSONObject processJSONObject = new ProcessJSONObject(jsonObject);
				String processType = processJSONObject.getProcessType();

				if (processType.equals(REGISTER)) { // Registration
					onRegister(processJSONObject.getUserInfo());
					System.out.println(processJSONObject.getUserInfo().getPeerId() + "Register terminal information for");
					showInfo(processJSONObject.getUserInfo(), REGISTER);
				} else if (processType.equals(UPDATE)) { // Update
					onUpdate(processJSONObject.getUserInfo());
					System.out.println(processJSONObject.getUserInfo().getPeerId() + "Update terminal information for");
					showInfo(processJSONObject.getUserInfo(), UPDATE);

				} else if (processType.equals(SEARCH)) { // Searching for
					onSearch(processJSONObject.getUserInfo(), processJSONObject.getSearchDistance());
					System.out.println(processJSONObject.getUserInfo().getPeerId() + "Execute search request from");

				} else if (processType.equals(DELETE)) { // Deletion
					onDelete(processJSONObject.getUserInfo());
					System.out.println(processJSONObject.getUserInfo().getPeerId() + "Delete terminal information for");

				}
			} catch (Exception e) {

			}
		}
	}

	/**
	 * Registers a user in the userInfoList
	 *
	 * @param userInfo Information of the user
	 */
	public void onRegister(UserInfo userInfo) {
		userInfoList.add(userInfo);
		System.out.println("REGISTER: Size of current userInfos" + userInfoList.size());
		//System.out.println(userInfo);
	}

	/**
	 * Updates user information in the userInfoList
	 *
	 * @param userInfo Updated new information of the user
	 */
	public void onUpdate(UserInfo userInfo) {
		/*
		if(userInfoList.contains(userInfo)) { // If included
			int index = userInfoList.indexOf(userInfo); // element number extraction
			userInfoList.set(index, userInfo);
			//System.out.println(index);
			System.out.println("Updated");
			//System.out.println(userInfo);
		}
		*/

		for (int i = 0; i < userInfoList.size(); i++) {
			if (userInfo.getPublicIP().equals(userInfo.getPublicIP()) && userInfoList.get(i).getPublicPort() == userInfo.getPublicPort() &&
					userInfoList.get(i).getPrivateIP().equals(userInfo.getPrivateIP()) && userInfoList.get(i).getPrivatePort() == userInfo.getPrivatePort()) {
				userInfoList.set(i, userInfo);
				break;
			}
		}
	}

	/**
	 * 1.Search user information that exists from the specified user's location or within the search radius circle, and store the corresponding user in the search list.
	 * 2.Send the IP and port of the user to be connected to for NAT traversal to the users stored in the search list, and have them send empty data such as "ping"
	 * 3.Return search results to the sending user
	 *
	 * @param userInfo       User information of the search source
	 * @param searchDistance Search radius
	 */
	public void onSearch(UserInfo userInfo, double searchDistance) {
		ArrayList<UserInfo> searchResultUserList = new ArrayList<>();

		//System.out.println(userInfo.getPeerId());
		//String myPeerID = userInfo.getPeerId();
		for (UserInfo item : userInfoList) {
			HubenyDistance hubenyDistance = new HubenyDistance();

			//Distance Calculation
			double distance = hubenyDistance.calcDistance(userInfo.getLatitude(), userInfo.getLongitude(),
					item.getLatitude(), item.getLongitude());

			//Detect users within range
			if (distance <= searchDistance) {
				// Will be replaced by if(item.getPeerId() == myPeerId)
				// Exclude yourself from search results
				if (item.getPublicIP().equals(userInfo.getPublicIP())
						&& item.getPublicPort() == userInfo.getPublicPort()
						&& item.getPrivateIP().equals(userInfo.getPrivateIP())
						&& item.getPrivatePort() == userInfo.getPrivatePort()) {
				} else {

					System.out.println(userInfo.getPeerId() + "Latitude:" + userInfo.getLatitude() + ", Longitude:" + userInfo.getLongitude());
					System.out.println(item.getPeerId() + "Latitude:" + item.getLatitude() + ", Longitude:" + item.getLongitude());
					System.out.println(userInfo.getPeerId() + "Peer ID:" + item.getPeerId() + ", Distance between terminals:" + distance + "m");
					searchResultUserList.add(item);
				}
			}
		}

		//System.out.println("MainActivity_onSearch - Number of search results:" + searchResult.size());

		// Next, it is necessary to register the information of the retrieving user with the NAT for P2P communication, so it prompts the user to do this.
		// Here, the Send class is called to return search results to the calling user. The arguments are publicIP, publicPort and searchResult

		SignalingServerSend UDPHolePunchingOtherUsers = new SignalingServerSend(socket, userInfo, searchResultUserList, "srcAddrPortRegisterToNat");
		UDPHolePunchingOtherUsers.start();
		System.out.println("");
		System.out.println(userInfo.getPeerId() + "srcAddrPortRegisterToNat started");
		SignalingServerSend reply = new SignalingServerSend(socket, userInfo, searchResultUserList, "replyFromMainActivity");
		reply.start();
		System.out.println(userInfo.getPeerId() + "replyFromMainActivity started");
		System.out.println("");
	}

	/**
	 * Discard specified user information from the userInfoList
	 *
	 * @param userInfo User information of the user that needs to be deleted
	 */
	public void onDelete(UserInfo userInfo) {
		/*
		if(userInfoList.contains(userInfo)) { // If it is the case that
			int index = userInfoList.indexOf(userInfo); // Element number extraction
			userInfoList.remove(index);
		}
		*/

		for (int i = 0; i < userInfoList.size(); i++) {
			if (userInfoList.get(i).getPublicIP().equals(userInfo.getPublicIP()) && userInfoList.get(i).getPublicPort() == userInfo.getPublicPort() &&
					userInfoList.get(i).getPrivateIP().equals(userInfo.getPrivateIP()) && userInfoList.get(i).getPrivatePort() == userInfo.getPrivatePort()) {
				userInfoList.remove(i);
				break;
			}
		}
	}

	/**
	 * Use when you want to see information
	 *
	 * @param userInfo User Information
	 */
	public void showInfo(UserInfo userInfo, String type) {
		System.out.println();
		System.out.println("--------- " + type + " -----------");
		System.out.println("Public IP???" + userInfo.getPublicIP());
		System.out.println("Public Port???" + userInfo.getPublicPort());
		System.out.println("Private IP???" + userInfo.getPrivateIP());
		System.out.println("Private Port???" + userInfo.getPrivatePort());
		System.out.println("Latitude???" + userInfo.getLatitude());
		System.out.println("Longitude???" + userInfo.getLongitude());
		System.out.println("PeerID???" + userInfo.getPeerId());
		//System.out.println("Speed???" + userInfo.getSpeed());
		System.out.println("---------------------------");
		System.out.println();
	}
}
