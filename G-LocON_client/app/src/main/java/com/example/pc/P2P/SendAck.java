package com.example.pc.P2P;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/*
 * Created by MF17037 on 2018/08/09.
 */
/*

public class SendAck extends AsyncTask<String, String, Integer> {
    private DatagramSocket socket;
    private int locationUpdateCount;
    private String srcIP;
    private int srcPort;
    private EP2PProcess eP2PProcess;

    public SendAck(DatagramSocket socket, int locationUpdateCount, String srcIP,int srcPort,EP2PProcess eP2PProcess){
        this.socket = socket;
        this.locationUpdateCount = locationUpdateCount;
        this.srcIP = srcIP;
        this.srcPort = srcPort;
        this.eP2PProcess = eP2PProcess;
    }


    @Override
    protected Integer doInBackground(String... data) {
        if (EP2PProcess.Ack.equals(eP2PProcess)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("processType", "Ack");
                jsonObject.put("locationUpdateCount", locationUpdateCount);
                byte[] sendData = jsonObject.toString().getBytes();

                DatagramPacket sendPacket;
                sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(srcIP), srcPort);
                socket.send(sendPacket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
}
*/
