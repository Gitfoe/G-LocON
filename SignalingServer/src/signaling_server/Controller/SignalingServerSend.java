package signaling_server.Controller;

import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.SecureRandom;
import java.util.ArrayList;

import org.json.JSONObject;
import signaling_server.Model.UserInfo;
import signaling_server.Model.UserSettings;

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
            UserInfo privacyUserInfo = applyPrivacyMeasuresToUserInfo(userInfo);
            JSONObject jsonObject = processJSONObject.getSrcUserInfo(privacyUserInfo);
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
            ArrayList<UserInfo> privacyMeasuresUserInfoList = applyPrivacyMeasuresToUserInfoInList(userInfoList);
            JSONObject jsonObject = processJSONObject.getUserInfoList(privacyMeasuresUserInfoList);
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

    //region Privacy methods
    /**
     * Applies all the privacy measures to users in a list.
     * @param userInfoList The list of user info's.
     * @return A copy of the list of users with applied privacy measures.
     */
    private ArrayList<UserInfo> applyPrivacyMeasuresToUserInfoInList(ArrayList<UserInfo> userInfoList) {
        ArrayList<UserInfo> copyUserInfoList = new ArrayList<UserInfo>();
        for (int i = 0; i < userInfoList.size(); i++) {
            copyUserInfoList.add(applyPrivacyMeasuresToUserInfo(userInfoList.get(i)));
        }
        return copyUserInfoList;
    }

    /**
     * Apply all privacy measures to a user.
     * @param userInfo The user that needs to get their privacy settings applied.
     * @return A copy of the userInfo with applied privacy settings.
     */
    private UserInfo applyPrivacyMeasuresToUserInfo(UserInfo userInfo) {
        UserInfo copyUserInfo = userInfo;
        copyUserInfo = removePersonalDataFromUserInfoAccordingToUserSettings(userInfo, true);
        copyUserInfo = anonymizeUser(copyUserInfo);
        return copyUserInfo;
    }

    /**
     * Anonymizes the peer ID parameter of a single user by replacing it with a random string of 40 characters.
     * @param userInfo The user that needs their peer ID anonymized.
     * @returns A copy of the user with an anonymized peer ID.
     */
    public UserInfo anonymizeUser(UserInfo userInfo) {
        // Generate random string of 40 characters to be used as a peer ID
        SecureRandom randomGenerator = new SecureRandom();
        byte[] randomBytes = new byte[20];
        randomGenerator.nextBytes(randomBytes);
        String randomString = new BigInteger(1, randomBytes).toString(16);
        // Create copy of userInfo with anonymized peerID
        return new UserInfo(userInfo.getPublicIP(), userInfo.getPublicPort(), userInfo.getPrivateIP(),
                userInfo.getPrivatePort(), userInfo.getLatitude(), userInfo.getLongitude(), randomString);
    }

    /**
     * Removes certain personal data from UserInfo according to the settings of that user.
     * @param userInfo The user that has its settings to be configured for.
     * @param li_enabled_override Override for forcing li_enabled to off, which means the location information is never sent,
     *                            even if the user has it enabled. This setting was implemented for compliance
     *                            with the "data minimalization" principle of the GDPR.
     * @return A copy of the userInfo with configured settings.
     */
    public UserInfo removePersonalDataFromUserInfoAccordingToUserSettings(UserInfo userInfo, boolean li_enabled_override) {
        // Obtain user settings and create copy of userInfo
        UserSettings settingsOfUser = DatabaseConnector.obtainUserSettings(userInfo);
        UserInfo copyUserInfo = new UserInfo(userInfo.getPublicIP(), userInfo.getPublicPort(), userInfo.getPrivateIP(),
                userInfo.getPrivatePort(), userInfo.getLatitude(), userInfo.getLongitude(), userInfo.getPeerId());
        // Apply user settings to userInfo
        if (!settingsOfUser.isLi_enabled() || li_enabled_override) { // Remove latitude and longitude information if li_enabled is false, or overridden
            copyUserInfo.setLatitude(null);
            copyUserInfo.setLongitude(null);
        }
        return copyUserInfo;
    }
    //endregion
}
