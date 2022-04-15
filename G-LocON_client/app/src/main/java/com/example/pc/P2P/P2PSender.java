package com.example.pc.P2P;

import android.os.AsyncTask;
import android.util.Log;

import com.example.pc.main.UserInfo;

import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by pc on 2018/06/09.
 */

public class P2PSender extends AsyncTask<String, String, Integer> {
    private DatagramSocket socket;
    private int locationUpdateCount;
    private UserInfo myUserInfo;
    private ArrayList<UserInfo> peripheralUsers;
    private EP2PProcess eP2PProcess;

    P2PSender(DatagramSocket socket,int locationUpdateCount,UserInfo myUserInfo,ArrayList<UserInfo> peripheralUsers,EP2PProcess eP2PProcess){
        this.socket = socket;
        this.locationUpdateCount = locationUpdateCount;
        this.myUserInfo = myUserInfo;
        this. peripheralUsers = peripheralUsers;
        this.eP2PProcess = eP2PProcess;
    }

    @Override
    protected void onPreExecute() {
    }

    /**
     * 配列[0]はIP,[1]はport番号を入れるものとする
     *
     * @return
     */
    @Override
    protected Integer doInBackground(String... data) {
        if(EP2PProcess.SendLocation.equals(eP2PProcess)) {
            Log.d("P2PSender_sendMsg", "現在の周辺ピア数：" + peripheralUsers.size());
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("processType", "SendLocation");
                jsonObject.put("locationUpdateCount",locationUpdateCount);
                jsonObject.put("latitude", myUserInfo.getLatitude());
                jsonObject.put("longitude",myUserInfo.getLongitude());
                jsonObject.put("peerID", myUserInfo.getPeerId()); //1112 Dを大文字にしてみた
                jsonObject.put("speed",myUserInfo.getSpeed());
                byte[] sendData = jsonObject.toString().getBytes();
                for (int i = 0; i < peripheralUsers.size(); i++) {
                    DatagramPacket sendPacket;
                    //同NAT内に存在する端末の場合はプライベートIPとPORTを指定する
                    if (myUserInfo.getPublicIP().equals(peripheralUsers.get(i).getPublicIP())) {
                        sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(peripheralUsers.get(i).getPrivateIP()), peripheralUsers.get(i).getPrivatePort());
                        socket.send(sendPacket);
                    }
                    //異なるNATに存在する端末の場合はNAT通過処理を行う
                    else {
                        Log.d("P2PSender_sendMsg", "宛先IP:" + peripheralUsers.get(i).getPublicIP() + "であり宛先PORT" + peripheralUsers.get(i).getPublicPort());
                        sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(peripheralUsers.get(i).getPublicIP()), peripheralUsers.get(i).getPublicPort());
                        socket.send(sendPacket);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return 0;
    }

    /**
     * 完了処理
     */
    @Override
    protected void onPostExecute(Integer a) {
        Log.d("P2P", "P2Psenderのsendはできた");
    }
}
