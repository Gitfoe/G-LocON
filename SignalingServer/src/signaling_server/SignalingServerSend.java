package signaling_server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import org.json.JSONObject;

public class SignalingServerSend extends Thread {
    private DatagramSocket socket; // UDP communication socket
    private UserInfo userInfo; // Source User Information
    private ArrayList<UserInfo> userInfoList; // NAT transit and other user information list
    private String replyData;

    /**
     * Constructor (no arguments)
     */
    public SignalingServerSend() {
        super();
    }

    /**
     * Constructor (with arguments)
     */
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
         * For srcAddrPortRegisterToNat
         * Converts the source user's UserInfo class to a JSONObject and sends the JSONObject to the users in the corresponding range (userInfoList).
         * After receiving the data, the relevant user stores the sender's Addr and port in the NAT.
         */
        if (replyData.equals("srcAddrPortRegisterToNat")) {
            ProcessJSONObject processJSONObject = new ProcessJSONObject();
            JSONObject jsonObject = processJSONObject.getSrcUserInfo(userInfo);
            try {
                byte[] sendData = jsonObject.toString().getBytes();
                DatagramPacket sendPacket;
                for (UserInfo item : userInfoList) {
                    sendPacket = new DatagramPacket(sendData, sendData.length,
                            InetAddress.getByName(item.getPublicIP()), item.getPrivatePort());
                    socket.send(sendPacket);
                    System.out.println("srcAddrPortRegisterToNat - Transmission of " + userInfo.getPeerId() + " (own terminal) information to users in the relevant range completed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * For replyFromMainActivity
         * Return search results to the sender user
         * Search results are converted to JSONObjects before being sent.
         */
        else if (replyData.equals("replyFromMainActivity")) {
            ProcessJSONObject processJSONObject = new ProcessJSONObject();
            JSONObject jsonObject = processJSONObject.getUserInfoList(userInfoList);
            try {
                byte[] sendData = jsonObject.toString().getBytes();
                DatagramPacket sendPacket;
                sendPacket = new DatagramPacket(sendData,
                        sendData.length, InetAddress.getByName(userInfo.getPublicIP()), userInfo.getPublicPort());
                socket.send(sendPacket);
                System.out.println("replyFromMainActivity - Search results sent to " + userInfo.getPeerId() + " (own terminal) completed");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
