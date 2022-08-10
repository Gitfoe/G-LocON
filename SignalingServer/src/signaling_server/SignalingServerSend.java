package signaling_server;

import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.SecureRandom;
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
         * For srcAddrPortRegisterToNat.
         * Converts the source user's UserInfo class to a JSONObject and sends the JSONObject to the users in the corresponding range (userInfoList).
         * After receiving the data, the relevant user stores the sender's Addr and port in the NAT.
         */
        if (replyData.equals("srcAddrPortRegisterToNat")) {
            ProcessJSONObject processJSONObject = new ProcessJSONObject();
            UserInfo anonymizedUserInfo = anonymizeUser(userInfo);
            JSONObject jsonObject = processJSONObject.getSrcUserInfo(anonymizedUserInfo);
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
         * For replyFromMainActivity.
         * Return search results to the sender user.
         * Search results are converted to JSONObjects before being sent.
         */
        else if (replyData.equals("replyFromMainActivity")) {
            ProcessJSONObject processJSONObject = new ProcessJSONObject();
            ArrayList<UserInfo> anonymizedUserInfoList = anonymizeUsersInList(userInfoList);
            JSONObject jsonObject = processJSONObject.getUserInfoList(anonymizedUserInfoList);
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

        /**
         * For sendUserSettings.
         * Retrieves the user settings from the database and sends it to the user.
         * Settings are converted to JSONObjects before being sent.
         */
        else if (replyData.equals("sendUserSettings")) {
            ProcessJSONObject processJSONObject = new ProcessJSONObject();
            UserSettings userSettings = DatabaseConnector.obtainUserSettings(userInfo);
            JSONObject jsonObject = processJSONObject.getSrcUserSettings(userSettings);
            try {
                byte[] sendData = jsonObject.toString().getBytes();
                DatagramPacket sendPacket;
                sendPacket = new DatagramPacket(sendData,
                        sendData.length, InetAddress.getByName(userInfo.getPublicIP()), userInfo.getPublicPort());
                socket.send(sendPacket);
                System.out.println("sendUserSettings - Setting data sent to " + userInfo.getPeerId() + " (own terminal)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Anonymizes the peer ID parameter of a single user.
     * @param userInfo The user that needs their peer ID anonymized.
     * @returns A copy of the user with an anonymized peer ID.
     */
    private UserInfo anonymizeUser(UserInfo userInfo) {
        SecureRandom randomGenerator = new SecureRandom();
        byte[] randomBytes = new byte[20];
        randomGenerator.nextBytes(randomBytes);
        String randomString = new BigInteger(1, randomBytes).toString(16);
        UserInfo anonymizedUserInfo = new UserInfo(userInfo.getPublicIP(), userInfo.getPublicPort(), userInfo.getPrivateIP(),
                userInfo.getPrivatePort(), userInfo.getLatitude(), userInfo.getLongitude(), randomString);
        return anonymizedUserInfo;
    }

    /**
     * Anonymizes the peer ID's in a list of userInfo objects.
     * @param userInfoList The list that has to be anonymized.
     * @return A copy of the userInfoList with anonymized users.
     */
    private ArrayList<UserInfo> anonymizeUsersInList(ArrayList<UserInfo> userInfoList) {
        ArrayList<UserInfo> anonymizedUserInfoList = new ArrayList<>();
        userInfoList.forEach(x -> anonymizedUserInfoList.add(anonymizeUser(x)));
        return anonymizedUserInfoList;
    }
}
