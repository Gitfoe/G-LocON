package com.example.pc.STUNServerClient;

/*
 * Created by pc on 2018/06/09.
 */

public interface ISTUNServerClientReceiver {
    void onReceiveMsgFromStun(String addr,int port);
}
