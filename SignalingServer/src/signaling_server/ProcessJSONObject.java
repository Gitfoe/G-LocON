package signaling_server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProcessJSONObject {
    private JSONObject jsonObject;

    /**
     * Empty constructor
     */
    ProcessJSONObject() {
    }

    /**
     * Constructor with args
     */
    ProcessJSONObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    /**
     * Get the processing type from the received data
     *
     * @return Processing type
     */
    public String getProcessType() {
        String processType = "";
        try {
            processType = jsonObject.getString("processType");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return processType;
    }

    /**
     * Mold and return user information
     *
     * @return UserInfo of the sender
     */
    public UserInfo getUserInfo() {
        UserInfo userInfo = new UserInfo();
        try {
            userInfo.setPublicIP(jsonObject.getString("publicIP"));
            userInfo.setPublicPort(jsonObject.getInt("publicPort"));
            userInfo.setPrivateIP(jsonObject.getString("privateIP"));
            userInfo.setPrivatePort(jsonObject.getInt("privatePort"));
            userInfo.setLatitude(jsonObject.getDouble("latitude"));
            userInfo.setLongitude(jsonObject.getDouble("longitude"));
            userInfo.setPeerId(jsonObject.getString("peerID"));
            //userInfo.setSpeed(jsonObject.getDouble("speed"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    /**
     * Get search range from json
     *
     * @return Cable range
     */
    public double getSearchDistance() {
        double searchDistance = 0.0;
        try {
            searchDistance = jsonObject.getDouble("searchDistance");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return searchDistance;
    }

    /**
     * UserInfos (user information list) to JSONObject
     *
     * @param userInfos Peripheral User Information
     * @return Formed JSONObject
     */
    public JSONObject getUserInfoList(ArrayList<UserInfo> userInfos) {
        JSONObject jsonObject = new JSONObject();
        JSONArray userList = new JSONArray();

        for (int i = 0; i < userInfos.size(); i++) {
            UserInfo userInfo = userInfos.get(i);
            JSONObject user = new JSONObject();
            try {
                user.put("publicIP", userInfo.getPublicIP());
                user.put("publicPort", userInfo.getPublicPort());
                user.put("privateIP", userInfo.getPrivateIP());
                user.put("privatePort", userInfo.getPrivatePort());
                user.put("latitude", userInfo.getLatitude());
                user.put("longitude", userInfo.getLongitude());
                user.put("peerID", userInfo.getPeerId());
                //user.put("speed", userInfo.getSpeed());
                userList.put(user);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            jsonObject.put("processType", "getPeripheralUserInfoList");
            jsonObject.put("userList", userList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * Make the sender's user information into a JSONObject.
     *
     * @param userInfo Source User Information.
     * @return userInfo as JSON.
     */
    public JSONObject getSrcUserInfo(UserInfo userInfo) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("processType", "doUDPHolePunching");
            jsonObject.put("publicIP", userInfo.getPublicIP());
            jsonObject.put("publicPort", userInfo.getPublicPort());
            jsonObject.put("privateIP", userInfo.getPrivateIP());
            jsonObject.put("privatePort", userInfo.getPrivatePort());
            jsonObject.put("latitude", userInfo.getLatitude());
            jsonObject.put("longitude", userInfo.getLongitude());
            jsonObject.put("peerID", userInfo.getPeerId());
            //jsonObject.put("speed",userInfo.getSpeed());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * Make the sender's user settings into a JSONObject.
     * @param userSettings The settings obtained from the database of the user.
     * @return userSettings as JSON.
     */
    public JSONObject getSrcUserSettings(UserSettings userSettings) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("processType", "getSrcUserSettings");
            jsonObject.put("peer_id", userSettings.getPeer_id());
            jsonObject.put("li_enabled", userSettings.isLi_enabled());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}