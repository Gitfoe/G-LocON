package com.example.pc.main;

import android.app.Application;
import android.content.Context;

/**
 * Created by pc on 2018/06/09.
 */

public class UtilCommon extends Application {
    private static Context context;
    private String signalingServerIP;
    private int signalingServerPort;
    private String stunServerIP;
    private int stunServerPort;
    private String peerId;

    public void onCreate(){
        super.onCreate();
        UtilCommon.context = getApplicationContext();
    }

    public static Context getAppContext(){
        return UtilCommon.context;
    }

    public String getSignalingServerIP() {
        return signalingServerIP;
    }

    public int getSignalingServerPort() {
        return signalingServerPort;
    }

    public String getStunServerIP() {
        return stunServerIP;
    }

    public int getStunServerPort() {
        return stunServerPort;
    }

    public String getPeerId() {
        return peerId;
    }

    public void setSignalingServerIP(String signalingServerIP) {
        this.signalingServerIP = signalingServerIP;
    }

    public void setSignalingServerPort(int signalingServerPort) {
        this.signalingServerPort = signalingServerPort;
    }

    public void setStunServerIP(String stunServerIP) {
        this.stunServerIP = stunServerIP;
    }

    public void setStunServerPort(int stunServerPort) {
        this.stunServerPort = stunServerPort;
    }

    public void setPeerId(String peerId) {
        this.peerId = peerId;
    }
}
