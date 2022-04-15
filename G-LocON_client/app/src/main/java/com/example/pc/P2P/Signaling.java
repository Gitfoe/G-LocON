package com.example.pc.P2P;
import android.os.AsyncTask;

import com.example.pc.main.UserInfo;
import com.example.pc.main.UtilCommon;

import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
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
     * シグナリングサーバにデータを送信する
     * @param data 今回は使わない
     * @return
     */
    @Override
    protected Integer doInBackground(String... data) {
        UtilCommon utilCommon = (UtilCommon)UtilCommon.getAppContext();
        SignalingJSONObject signalingJSONObject = new SignalingJSONObject();
        JSONObject jsonObject;

        switch(eSignalingProcess){
            case REGISTER:
                jsonObject = signalingJSONObject.covUserInfoForRegister(userInfo);
                try {
                    byte[] sendData = jsonObject.toString().getBytes();
                    DatagramPacket sendPacket;
                    sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(utilCommon.getSignalingServerIP()), utilCommon.getSignalingServerPort());
                    socket.send(sendPacket);
                    System.out.println("REGISTER送信完了");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case UPDATE:
                jsonObject = signalingJSONObject.covUserInfoForUpdate(userInfo);
                try {
                    byte[] sendData = jsonObject.toString().getBytes();
                    DatagramPacket sendPacket;
                    sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(utilCommon.getSignalingServerIP()), utilCommon.getSignalingServerPort());
                    socket.send(sendPacket);
                    System.out.println("UPDATE送信完了");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case SEARCH:
                jsonObject = signalingJSONObject.covUserInfoForSearch(userInfo, searchDistance);
                try {
                    byte[] sendData = jsonObject.toString().getBytes();
                    DatagramPacket sendPacket;
                    sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(utilCommon.getSignalingServerIP()), utilCommon.getSignalingServerPort());
                    socket.send(sendPacket);
                    System.out.println("SEARCH送信完了");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case DELETE:
                jsonObject = signalingJSONObject.covUserInfoForDelete(userInfo);
                try {
                    byte[] sendData = jsonObject.toString().getBytes();
                    DatagramPacket sendPacket;
                    sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(utilCommon.getSignalingServerIP()), utilCommon.getSignalingServerPort());
                    socket.send(sendPacket);
                    System.out.println("DELETE送信完了");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
        return 0;
    }

    /**
     * 完了処理
     */
    @Override
    protected void onPostExecute(Integer a) {
    }

}
