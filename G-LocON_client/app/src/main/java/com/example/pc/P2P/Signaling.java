package com.example.pc.P2P;
import android.os.AsyncTask;

import com.example.pc.main.UserInfo;
import com.example.pc.main.UtilCommon;

import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/*
 * Created by pc on 2018/06/09.
 */

public class Signaling extends AsyncTask<String, String, Integer> {
    private DatagramSocket socket;
    private UserInfo userInfo;
    private ESignalingProcess eSignalingProcess;
    private double searchDistance;

    Signaling(DatagramSocket socket, UserInfo userInfo,ESignalingProcess eSignalingProcess) {
        this.socket = socket;
        this.userInfo = userInfo;
        this.eSignalingProcess = eSignalingProcess;
    }

    Signaling(DatagramSocket socket,UserInfo userInfo,double searchDistance,ESignalingProcess eSignalingProcess) {
        this.socket = socket;
        this.userInfo = userInfo;
        this.searchDistance = searchDistance;
        this.eSignalingProcess = eSignalingProcess;
    }

    @Override
    protected void onPreExecute() {
    }

    /**
     * Send data to signaling server
     * @param data I won't use it this time
     */
    @Override
    protected Integer doInBackground(String... data) {
        UtilCommon utilCommon = (UtilCommon)UtilCommon.getAppContext();
        SignalingJSONObject signalingJSONObject = new SignalingJSONObject();
        JSONObject jsonObject;

        switch(eSignalingProcess){
            case SEARCH:
                jsonObject = signalingJSONObject.covUserInfoForSearch(userInfo, searchDistance);
                sendToSignalingServer(jsonObject, utilCommon, eSignalingProcess);
                break;

            default: // Default case is sufficient for all other signaling processes
                jsonObject = signalingJSONObject.covUserInfo(userInfo, eSignalingProcess);
                sendToSignalingServer(jsonObject, utilCommon, eSignalingProcess);
                break;
        }
        return 0;
    }

    private void sendToSignalingServer(JSONObject jsonObject, UtilCommon utilCommon, ESignalingProcess eSignalingProcess) {
        try {
            byte[] sendData = jsonObject.toString().getBytes();
            DatagramPacket sendPacket;
            sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(utilCommon.getSignalingServerIP()), utilCommon.getSignalingServerPort());
            socket.send(sendPacket);
            System.out.println(eSignalingProcess.name() + " transmission complete");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Completion procedures
     */
    @Override
    protected void onPostExecute(Integer a) {
    }
}