package com.example.pc.STUNServerClient;

import android.os.AsyncTask;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by pc on 2018/06/09.
 */

public class STUNServerClientReceiver extends AsyncTask<String, String, Integer> {
    private ISTUNServerClientReceiver istunServerClientReceiver;
    private DatagramSocket socket;

    STUNServerClientReceiver(DatagramSocket socket, ISTUNServerClientReceiver istunServerClientReceiver) {
        this.socket = socket;
        this.istunServerClientReceiver = istunServerClientReceiver;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Integer doInBackground(String... text) {
        // receive Data
        DatagramPacket receivePacket = new DatagramPacket(new byte[128], 128);
        String addr;
        int port;
        try {
            socket.receive(receivePacket);
            String allData = new String(receivePacket.getData(), 0, receivePacket.getLength());
            Log.d("UDP_HOLE_PUNCHING", allData);
            String result[] = allData.split("-", 0);
            addr = result[0];
            port = Integer.parseInt(result[1]);
            istunServerClientReceiver.onReceiveMsgFromStun(addr, port);

            } catch (Exception e) {
                Log.d("hogehoge", "変換で失敗" + e);
            }
        return 0;
    }

    @Override
    protected void onPostExecute(Integer result) {
    }
}
