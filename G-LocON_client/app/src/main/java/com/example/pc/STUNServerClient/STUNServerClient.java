package com.example.pc.STUNServerClient;

import android.os.AsyncTask;

import java.net.DatagramSocket;

/**
 * Created by pc on 2018/06/09.
 */

public class STUNServerClient implements ISTUNServerClientSender,ISTUNServerClientReceiver{
    private ISTUNServerClient istunServerClient;
    private DatagramSocket socket;

    public STUNServerClient(DatagramSocket socket,ISTUNServerClient istunServerClient){
        this.socket = socket;
        this.istunServerClient = istunServerClient;
    }


    public void stunServerClientStart(){
        STUNServerClientSender stunServerClientSender = new STUNServerClientSender(socket,this);
        stunServerClientSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onSendFinishMsgToStun(){
        STUNServerClientReceiver stunServerClientReceiver = new STUNServerClientReceiver(socket,this);
        stunServerClientReceiver.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public void onReceiveMsgFromStun(String addr, int port){
        istunServerClient.onGetGlobalIP_Port(addr,port);
    }
}
