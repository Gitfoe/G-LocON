package com.example.pc.P2P;

import android.os.AsyncTask;
import android.util.Log;

import com.example.pc.main.UserInfo;

import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

/*
 * Created by pc on 2018/06/09.
 */

/**
 * Class for recording the IP and port of the communication partner to NAT
 * The purpose of the packet is to send a packet from
 */
public class P2PNatRegisterSender extends AsyncTask<String, String, Integer> {
    private DatagramSocket socket;
    private String publicIP;
    private int publicPort;
    private EP2PProcess eP2PProcess;
    private ArrayList<UserInfo> peripheralUsers;
    private UserInfo srcUser;

    P2PNatRegisterSender(DatagramSocket socket, String publicIP, int publicPort, ArrayList<UserInfo> peripheralUsers, EP2PProcess eP2PProcess) {
        this.socket = socket;
        this.publicIP = publicIP;
        this.publicPort = publicPort;
        this.peripheralUsers = peripheralUsers;
        this.eP2PProcess = eP2PProcess;
    }

    P2PNatRegisterSender(DatagramSocket socket, String publicIP, int publicPort, UserInfo srcUserInfo, EP2PProcess eP2PProcess) {
        this.socket = socket;
        this.publicIP = publicIP;
        this.publicPort = publicPort;
        this.srcUser = srcUserInfo;
        this.eP2PProcess = eP2PProcess;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Integer doInBackground(String... data) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("processType", "HelloPacket");// This will be ignored even if it reaches the other party.
        }catch (Exception e){
            e.printStackTrace();
        }
        byte[] sendData = jsonObject.toString().getBytes();

        switch(eP2PProcess) {
            case NATRegisterDstUsers:
                for (int i = 0; i < peripheralUsers.size(); i++) {
                    DatagramPacket sendPacket;
                    // Specify private IP and PORT for terminals residing in the same NAT
                    if (publicIP.equals(peripheralUsers.get(i).getPublicIP())) {
                        try {
                            sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(peripheralUsers.get(i).getPrivateIP()), peripheralUsers.get(i).getPrivatePort());
                            socket.send(sendPacket);
                        } catch (Exception e){
                        }
                    }
                    // For terminals residing on different NATs, NAT traversal processing is performed
                    else {
                        try {
                            sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(peripheralUsers.get(i).getPublicIP()), peripheralUsers.get(i).getPublicPort());
                            socket.send(sendPacket);
                        } catch (Exception e){
                        }
                    }
                }
                break;
            case NATRegisterSrcUser:

                DatagramPacket sendPacket;
                // Specify private IP and PORT for terminals residing in the same NAT
                if (publicIP.equals(srcUser.getPublicIP())) {
                    try {
                        sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(srcUser.getPrivateIP()), srcUser.getPrivatePort());
                        socket.send(sendPacket);
                    } catch (Exception e){
                    }
                }
                // If the terminal exists in a NAT that is a NAT, NAT transit processing is performed.
                else {
                    try {
                        sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(srcUser.getPublicIP()), srcUser.getPublicPort());
                        socket.send(sendPacket);
                    } catch (Exception e){
                    }
                }
                break;
            case SendLocation:
                break;
            default:
                break;
        }
        return 0;
    }

    @Override
    protected void onPostExecute(Integer a) {
        Log.d("P2P", "P2PNatRegisterSend send was done");
    }
}
