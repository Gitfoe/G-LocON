package signaling_server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

import org.json.JSONObject;

public class SignalingServerReceive extends Thread {

	//メンバ変数
	private DatagramSocket socket;
	private ArrayList<UserInfo> userInfoList;

	//コンストラクタ
	public SignalingServerReceive() { super(); }

	//コンストラクタ
	public SignalingServerReceive(DatagramSocket socket, ArrayList<UserInfo> userInfoList) {
		this.socket = socket;
		this.userInfoList = userInfoList;
	}

	@Override
	public void run() {
		//受信メッセージの定義
		final String REGISTER = "REGISTER";
		final String UPDATE = "UPDATE";
		final String SEARCH = "SEARCH";
		final String DELETE = "DELETE";

		while (true) {
			// receive Data
			DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
			try {
				socket.receive(receivePacket);

				String result = new String(receivePacket.getData(), 0, receivePacket.getLength());

				JSONObject jsonObject = new JSONObject(result);
				ProcessJSONObject processJSONObject = new ProcessJSONObject(jsonObject);
				String processType = processJSONObject.getProcessType();

				if (processType.equals(REGISTER)) { //登録
					onRegister(processJSONObject.getUserInfo());
					System.out.println(processJSONObject.getUserInfo().getPeerId() + "の端末情報を登録");
					showInfo(processJSONObject.getUserInfo(), REGISTER);
				}

				else if (processType.equals(UPDATE)) { //更新
					onUpdate(processJSONObject.getUserInfo());
					System.out.println(processJSONObject.getUserInfo().getPeerId() + "の端末情報の更新");
					showInfo(processJSONObject.getUserInfo(), UPDATE);

				} else if (processType.equals(SEARCH)) { //検索
					onSearch(processJSONObject.getUserInfo(), processJSONObject.getSearchDistance());
					System.out.println(processJSONObject.getUserInfo().getPeerId() + "からの検索要求を実行");

				} else if (processType.equals(DELETE)) { //削除
					onDelete(processJSONObject.getUserInfo());
					System.out.println(processJSONObject.getUserInfo().getPeerId() + "の端末情報を削除");

				}
			} catch (Exception e) {

			}
		}
	}

	/**
	 * ユーザ情報登録
	 * @param userInfo
	 */
	public void onRegister(UserInfo userInfo) {
		userInfoList.add(userInfo);
		System.out.println("REGISTER:現在のuserInfosのサイズ"+userInfoList.size());
		//System.out.println(userInfo);
	}

	/**
	 * ユーザ情報を更新する
	 * @param userInfo //ユーザ情報
	 */
	public void onUpdate(UserInfo userInfo) {
		/*
		if(userInfoList.contains(userInfo)) { //含まれている場合
			int index = userInfoList.indexOf(userInfo); //要素番号抽出
			userInfoList.set(index, userInfo);
			//System.out.println(index);
			System.out.println("更新しました");
			//System.out.println(userInfo);
		}
		*/
	       for(int i = 0; i < userInfoList.size(); i++){
	            if(userInfo.getPublicIP().equals(userInfo.getPublicIP()) && userInfoList.get(i).getPublicPort() == userInfo.getPublicPort() &&
	                    userInfoList.get(i).getPrivateIP().equals(userInfo.getPrivateIP()) && userInfoList.get(i).getPrivatePort() == userInfo.getPrivatePort()) {
	                userInfoList.set(i, userInfo);
	                break;
	            }
	        }

	}

	/**
	 * 1.指定されたユーザの地点からか検索半径の円に存在するユーザ情報を検索し，該当ユーザを検索リストに格納
	 * 2.検索リストに格納されたユーザにNAT通過のために接続先ユーザのIPとポートを送信し，"ping"のような空データを送信させる
	 * 3.送信元のユーザに検索結果を返却する
	 * @param userInfo 検索元のユーザ情報
	 * @param searchDistance 検索半径
	 */
	public void onSearch(UserInfo userInfo, double searchDistance) {
		ArrayList<UserInfo> searchResultUserList = new ArrayList<>();

		//System.out.println(userInfo.getPeerId());
		//String myPeerID = userInfo.getPeerId();
		for(UserInfo item : userInfoList) {
			HubenyDistance hubenyDistance = new HubenyDistance();

			//距離算出
			double distance = hubenyDistance.calcDistance(userInfo.getLatitude(), userInfo.getLongitude(),
					item.getLatitude(), item.getLongitude());
			//itemの中身見る用
			/*
			System.out.println();
			System.out.println("******************************");
			System.out.println("public IP：" + item.getPublicIP());
			System.out.println("public Port：" + item.getPublicPort());
			System.out.println("private IP：" + item.getPrivateIP());
			System.out.println("private Port：" + item.getPrivatePort());
			System.out.println("Latitude：" + item.getLatitude());
			System.out.println("Longitude：" + item.getLongitude());
			System.out.println("PeerID：" + item.getPeerId());
			//System.out.println("Speed"+item.getSpeed());
			System.out.println("******************************");
			System.out.println();
			*/

			//System.out.println(userInfo.getPeerId()+"の緯度は"+userInfo.getLatitude()+"  経度は"+userInfo.getLongitude());
			//System.out.println(item.getPeerId()+"の緯度は"+item.getLatitude()+"  経度は"+item.getLongitude());
			//System.out.println(userInfo.getPeerId()+"と"+item.getPeerId()+"の距離は"+distance+"m");
			//範囲内にいるユーザを検出
			if(distance <= searchDistance) {
				// if(item.getPeerId() == myPeerId)に置き換える予定
				// 自分自身を検索結果から除外
				if (item.getPublicIP().equals(userInfo.getPublicIP())
						&& item.getPublicPort() == userInfo.getPublicPort()
						&& item.getPrivateIP().equals(userInfo.getPrivateIP())
						&& item.getPrivatePort() == userInfo.getPrivatePort()) {
				}else {

					System.out.println(userInfo.getPeerId()+"の緯度は"+userInfo.getLatitude()+"  経度は"+userInfo.getLongitude());
					System.out.println(item.getPeerId()+"の緯度は"+item.getLatitude()+"  経度は"+item.getLongitude());
					System.out.println(userInfo.getPeerId()+"と"+item.getPeerId()+"の距離は"+distance+"m");
					//System.out.println("端末間の距離は" + distance + "m");
					searchResultUserList.add(item);
				}

			}
		}

		//System.out.println("MainActivity_onSearch:検索結果の個数:"+searchResult.size());

		//つぎにP2P通信を行うためにNATに検索元ユーザの情報を登録させる必要があるのでこれを行うよう促す
		//ここでSendクラスを呼び呼び出し元のユーザに検索結果を返す．引数はpublicIP,publicPort,searchResult

		SignalingServerSend UDPHolePunchingOtherUsers = new SignalingServerSend(socket, userInfo, searchResultUserList, "srcAddrPortRegisterToNat");
		UDPHolePunchingOtherUsers.start();
		System.out.println("");
		System.out.println(userInfo.getPeerId()+"のsrcAddrPortRegisterToNatをスタートしました");
		SignalingServerSend reply = new SignalingServerSend(socket, userInfo, searchResultUserList, "replyFromMainActivity");
		reply.start();
		System.out.println(userInfo.getPeerId()+"のreplyFromMainActivityをスタートしました");
		System.out.println("");
	}

	/**
	 * 指定されたユーザ情報を破棄する
	 * @param userInfo//ユーザ情報
	 */
	public void onDelete(UserInfo userInfo) {
		/*
		if(userInfoList.contains(userInfo)) { //含まれている場合
			int index = userInfoList.indexOf(userInfo); //要素番号抽出
			userInfoList.remove(index);
		}
		*/
        for(int i = 0; i < userInfoList.size(); i++){
            if(userInfoList.get(i).getPublicIP().equals(userInfo.getPublicIP()) && userInfoList.get(i).getPublicPort() == userInfo.getPublicPort() &&
                    userInfoList.get(i).getPrivateIP().equals(userInfo.getPrivateIP()) && userInfoList.get(i).getPrivatePort() == userInfo.getPrivatePort()) {
                userInfoList.remove(i);
                break;
            }
        }
	}


	/**
	 * 情報を見たいときに使用する
	 * @param userInfo//ユーザ情報
	 */
	public void showInfo(UserInfo userInfo, String type) {
		System.out.println();
		System.out.println("--------- " + type + " -----------");
		System.out.println("public IP：" + userInfo.getPublicIP());
		System.out.println("public Port：" + userInfo.getPublicPort());
		System.out.println("private IP：" + userInfo.getPrivateIP());
		System.out.println("private Port：" + userInfo.getPrivatePort());
		System.out.println("Latitude：" + userInfo.getLatitude());
		System.out.println("Longitude：" + userInfo.getLongitude());
		System.out.println("PeerID：" + userInfo.getPeerId());
		//System.out.println("speed："+userInfo.getSpeed());
		System.out.println("---------------------------");
		System.out.println();
	}

}
