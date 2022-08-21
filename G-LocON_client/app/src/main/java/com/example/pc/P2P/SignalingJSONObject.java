package com.example.pc.P2P;

import android.util.Log;

import com.example.pc.main.UserInfo;
import com.example.pc.main.UserSettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*
 * Created by pc on 2018/06/09.
 */

public class SignalingJSONObject {
    JSONObject jsonObject;

    SignalingJSONObject()  {

    }

    SignalingJSONObject(JSONObject jsonObject){
        this.jsonObject = jsonObject;
    }

    /**
     * Obtain the type of data from the received data.
     *  @return Processing type
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

    public ArrayList<UserInfo> getPeripheralUsers() {
        ArrayList<UserInfo> peripheralUsers = new ArrayList<>();
        JSONArray getArray = null;
        try {
            Log.d("ProcessJSONObject","I'm up to getPeripheralUserInfos.");
            System.out.println(jsonObject.toString(4));
            getArray = jsonObject.getJSONArray("userList");
            for(int i = 0; i < getArray.length(); i++) {
                UserInfo peripheralUser = new UserInfo();
                JSONObject obj = getArray.getJSONObject(i);
                peripheralUser.setPublicIP(obj.getString("publicIP"));
                peripheralUser.setPublicPort(obj.getInt("publicPort"));
                peripheralUser.setPrivateIP(obj.getString("privateIP"));

                // Additional check if the JSONObject contains values for latitude and longitude (might be disabled by userSettings)
                if (obj.has("latitude") && obj.has("longitude")) {
                    peripheralUser.setLatitude(obj.getDouble("latitude"));
                    peripheralUser.setLongitude(obj.getDouble("longitude"));
                }
                else { // Set values to null instead if they are not in the JSONObject
                    peripheralUser.setLatitude(null);
                    peripheralUser.setLatitude(null);
                }

                peripheralUser.setPrivatePort(obj.getInt("privatePort"));
                peripheralUser.setPeerId(obj.getString("peerID"));
                //peripheralUser.setSpeed(obj.getDouble("speed"));
                peripheralUsers.add(peripheralUser);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return peripheralUsers;
    }

    public UserInfo getSrcUser() {
        UserInfo srcUser = new UserInfo();
        try {
            Log.d("ProcessJSONObject","I'm up to getSrcUser.");
            srcUser.setPublicIP(jsonObject.getString("publicIP"));
            srcUser.setPublicPort(jsonObject.getInt("publicPort"));
            srcUser.setPrivateIP(jsonObject.getString("privateIP"));
            srcUser.setPrivatePort(jsonObject.getInt("privatePort"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  srcUser;
    }

    public UserSettings getSrcUserSettings() {
        UserSettings userSettings = new UserSettings();
        try {
            Log.d("ProcessJSONObject","I'm up to getSrcUserSettings.");
            userSettings.setPeer_id(jsonObject.getString("peer_id"));
            userSettings.setLi_enabled(jsonObject.getBoolean("li_enabled"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userSettings;
    }

    /**
     * Register UserInfo with the signaling server
     * @param userInfo User information
     * @return userInfo as JSON
     */
    public JSONObject covUserInfo(UserInfo userInfo, ESignalingProcess eSignalingProcess) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("processType", eSignalingProcess.name());
            jsonObject.put("publicIP",userInfo.getPublicIP());
            jsonObject.put("publicPort",userInfo.getPublicPort());
            jsonObject.put("privateIP",userInfo.getPrivateIP());
            jsonObject.put("privatePort",userInfo.getPrivatePort());
            jsonObject.put("latitude",userInfo.getLatitude());
            jsonObject.put("longitude",userInfo.getLongitude());
            jsonObject.put("peerID",userInfo.getPeerId());
            //jsonObject.put("speed",userInfo.getSpeed());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * Register UserSettings with the signaling server
     * @param userSettings User settings
     * @return userSettings as JSON
     */
    public JSONObject covUserSettings(UserSettings userSettings, ESignalingProcess eSignalingProcess) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("processType", eSignalingProcess.name());
            jsonObject.put("peer_id",userSettings.getPeer_id());
            jsonObject.put("li_enabled",userSettings.isLi_enabled());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * Query the signaling server for a search
     * @param userInfo User information
     * @return userInfo and distance as JSON
     */
    public JSONObject covUserInfoForSearch(UserInfo userInfo, double distance) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("processType","SEARCH");
            jsonObject.put("publicIP",userInfo.getPublicIP());
            jsonObject.put("publicPort",userInfo.getPublicPort());
            jsonObject.put("privateIP",userInfo.getPrivateIP());
            jsonObject.put("privatePort",userInfo.getPrivatePort());
            jsonObject.put("latitude",userInfo.getLatitude());
            jsonObject.put("longitude",userInfo.getLongitude());
            jsonObject.put("searchDistance",distance);
            jsonObject.put("peerID",userInfo.getPeerId());
            //jsonObject.put("speed",userInfo.getSpeed());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
