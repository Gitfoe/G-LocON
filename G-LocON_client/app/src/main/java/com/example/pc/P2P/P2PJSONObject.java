package com.example.pc.P2P;

import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * Created by pc on 2018/06/09.
 */

public class P2PJSONObject {
    JSONObject jsonObject;

    P2PJSONObject(JSONObject jsonObject){
        this.jsonObject = jsonObject;
    }

    /**
     * Get processing type
     * @return Processing method
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

    public String getPeerId(){
        String peerId = null;
        try {
            peerId = jsonObject.getString("peerID");
        }catch (Exception e){
        }
        return peerId;
    }

    public double getSpeed(){
        double speed = 0;
        try {
            speed = jsonObject.getDouble("speed");
        }catch (Exception e){
        }
        return speed;
    }

    public int getLocationCount(){
        int count = 0;
        try {
            count = jsonObject.getInt("locationUpdateCount");
        }catch (Exception e){
        }
        return count;
    }

    public Location getPeripheralUserLocation(){
        Location location = new Location("");
        try {
            location.setLatitude(jsonObject.getDouble("latitude"));
            location.setLongitude(jsonObject.getDouble("longitude"));
        }catch (Exception e){
        }
        return location;
    }
}
