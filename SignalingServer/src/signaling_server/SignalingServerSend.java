package signaling_server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import org.json.JSONObject;

public class SignalingServerSend extends Thread {
	private DatagramSocket socket;//UDP通信ソケット
	private UserInfo userInfo;//送信元ユーザ情報
	private ArrayList<UserInfo> userInfoList;//NAT通過その他ユーザ情報リスト
	private String replyData;

	//コンストラクタ（引数なし）
	public SignalingServerSend() {
		super();
	}

	//コンストラクタ（引数あり）
	public SignalingServerSend(DatagramSocket socket, UserInfo userInfo, ArrayList<UserInfo> userInfoList,
			String replyData) {
		this.socket = socket;
		this.userInfo = userInfo;
		this.userInfoList = userInfoList;
		this.replyData = replyData;
	}


    @Override
    public void run() {
        /**
         * srcAddrPortRegisterToNatの場合
         * 送信元ユーザのUserInfoクラスをJSONObjectに変換し，該当範囲のユーザ（userInfoList）にJSONObjectを送信する
         * 該当ユーザはデータを受信後，送信元ユーザのAddr,portをNATに記憶させる
         */
        if(replyData.equals("srcAddrPortRegisterToNat")){
            ProcessJSONObject processJSONObject = new ProcessJSONObject();
            JSONObject jsonObject = processJSONObject.getSrcUserInfo(userInfo);
            try {
                byte[] sendData = jsonObject.toString().getBytes();
                DatagramPacket sendPacket;
                for(UserInfo item : userInfoList) {
                	sendPacket = new DatagramPacket(sendData,sendData.length,
                			InetAddress.getByName(item.getPublicIP()), item.getPrivatePort());
                	socket.send(sendPacket);
                	System.out.println("srcAddrPortRegisterToNat-----該当範囲ユーザへの"+userInfo.getPeerId()+"(自端末)情報の送信完了");
                }
            } catch (Exception e) {
				e.printStackTrace();
			}
        }
        /**
         * replyFromMainActivityの場合
         * 送信元ユーザに検索結果を返す
         * 検索結果はJSONObjectに変換されてから送信される
         */
        else if(replyData.equals("replyFromMainActivity")){
            ProcessJSONObject processJSONObject = new ProcessJSONObject();
            JSONObject jsonObject = processJSONObject.getUserInfoList(userInfoList);
            try {
                byte[] sendData = jsonObject.toString().getBytes();
                DatagramPacket sendPacket;
                sendPacket = new DatagramPacket(sendData,
                        sendData.length, InetAddress.getByName(userInfo.getPublicIP()), userInfo.getPublicPort());
                socket.send(sendPacket);
                System.out.println("replyFromMainActivity-----"+userInfo.getPeerId()+"(自端末)への検索結果送信完了");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
