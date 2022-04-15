package com.example.pc.STUNServerClient;

import android.os.AsyncTask;
import android.util.Log;


import com.example.pc.main.UtilCommon;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by pc on 2018/06/09.
 */

public class STUNServerClientSender extends AsyncTask<String, String, Integer> {
    private DatagramSocket socket;
    ISTUNServerClientSender istunServerClientSender;

    STUNServerClientSender(DatagramSocket socket,ISTUNServerClientSender istunServerClientSender){
        this.socket = socket;
        this.istunServerClientSender = istunServerClientSender;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Integer doInBackground(String... ttt) {
        String sendMsg = "Hello";
        UtilCommon utilCommon = (UtilCommon)UtilCommon.getAppContext();
        String stunServerIP = utilCommon.getStunServerIP();
        int stunServerPort = utilCommon.getStunServerPort();
        while(true) {
            try {
                byte[] sendData = sendMsg.getBytes();
                DatagramPacket sendPacket;
                sendPacket = new DatagramPacket(sendData,
                        sendData.length, InetAddress.getByName(stunServerIP), stunServerPort);
                socket.send(sendPacket);
                if(sendMsg.equals("Hello")){
                    sendMsg = "Ping";
                    istunServerClientSender.onSendFinishMsgToStun();
                }
                sleep();
            } catch (Exception e) {
                Log.d("loghogehoge", "" + e);
            }
        }
    }

    @Override
    protected void onPostExecute(Integer result) {

    }


    private void sleep(){
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
