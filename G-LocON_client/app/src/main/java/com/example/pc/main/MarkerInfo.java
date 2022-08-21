package com.example.pc.main;

import com.google.android.gms.maps.model.Marker;

/*
 * Created by pc on 2018/08/11.
 */

public class MarkerInfo {
    private Marker marker;
    private String publicIP;
    private int publicPort;
    private String privateIP;
    private int privatePort;

    MarkerInfo(Marker marker, String publicIP, int publicPort, String privateIP, int privatePort){
        this.marker = marker;
        this.publicIP = publicIP;
        this.publicPort = publicPort;
        this.privateIP = privateIP;
        this.privatePort = privatePort;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public String getPublicIP() {
        return publicIP;
    }

    public void setPublicIP(String publicIP) {
        this.publicIP = publicIP;
    }

    public int getPublicPort() {
        return publicPort;
    }

    public void setPublicPort(int publicPort) {
        this.publicPort = publicPort;
    }

    public String getPrivateIP() {
        return privateIP;
    }

    public void setPrivateIP(String privateIP) {
        this.privateIP = privateIP;
    }

    public int getPrivatePort() {
        return privatePort;
    }

    public void setPrivatePort(int privatePort) {
        this.privatePort = privatePort;
    }
}
