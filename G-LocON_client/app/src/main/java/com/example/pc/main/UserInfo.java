package com.example.pc.main;

/**
 * Created by MF17037 on 2017/12/13.
 */

public class UserInfo {
    private String publicIP;
    private int publicPort;
    private String privateIP;
    private int privatePort;
    private double latitude;
    private double longitude;
    private String peerId;
    private double speed;

    public UserInfo(){

    }


    public String getPublicIP() {
        return publicIP;
    }

    public int getPublicPort() {
        return publicPort;
    }

    public String getPrivateIP() {
        return privateIP;
    }

    public int getPrivatePort() {
        return privatePort;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getPeerId(){
        return peerId;
    }

    public double getSpeed() {
        return speed;
    }

    public void setPublicIP(String publicIP) {
        this.publicIP = publicIP;
    }

    public void setPublicPort(int publicPort) {
        this.publicPort = publicPort;
    }

    public void setPrivateIP(String privateIP) {
        this.privateIP = privateIP;
    }

    public void setPrivatePort(int privatePort) {
        this.privatePort = privatePort;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setPeerId(String peerId){
        this.peerId = peerId;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}

