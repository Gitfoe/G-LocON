package com.example.pc.P2P;

import android.util.Log;

import com.example.pc.main.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*
 * Created by pc on 2018/06/09.
 */

public class SignalingJSONObject {
    JSONObject jsonObject;

    SignalingJSONObject(){

    }

    SignalingJSONObject(JSONObject jsonObject){
        this.jsonObject = jsonObject;
    }

    /**
     * Obtain the type of data from the received data.
     *  @return Processing type
     */
    public String getProcessType(){
        String processType = "";
        try {
            processType = jsonObject.getString("processType");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return processType;
    }

    public ArrayList<UserInfo> getPerioheralUsers(){
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
                peripheralUser.setPrivatePort(obj.getInt("privatePort"));
                peripheralUser.setLatitude(obj.getDouble("latitude"));
                peripheralUser.setLongitude(obj.getDouble("longitude"));
                peripheralUser.setPeerId(obj.getString("peerID"));
                //peripheralUser.setSpeed(obj.getDouble("speed"));
                peripheralUsers.add(peripheralUser);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return peripheralUsers;
    }

    public UserInfo getSrcUser(){
        UserInfo srcUser = new UserInfo();
        try {
            srcUser.setPublicIP(jsonObject.getString("publicIP"));
            srcUser.setPublicPort(jsonObject.getInt("publicPort"));
            srcUser.setPrivateIP(jsonObject.getString("privateIP"));
            srcUser.setPrivatePort(jsonObject.getInt("privatePort"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  srcUser;
    }

    /**
     * Register UserInfo with the signaling server
     * @param userInfo User information
     * @return userInfo as JSON
     */
    public JSONObject covUserInfoForRegister(UserInfo userInfo){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("processType","REGISTER");
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
     * Update UserInfo to signaling server
     * @param userInfo User information
     * @return userInfo as JSON
     */
    public JSONObject covUserInfoForUpdate(UserInfo userInfo){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("processType","UPDATE");
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
     * Query the signaling server for a search
     * @param userInfo User information
     * @return userInfo and distance as JSON
     */
    public JSONObject covUserInfoForSearch(UserInfo userInfo, double distance){
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

    /**
     * Have signaling server destroy UserInfo
     * @param userInfo User information
     * @return userInfo as JSON
     */
    public JSONObject covUserInfoForDelete(UserInfo userInfo){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("processType","DELETE");
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
}
