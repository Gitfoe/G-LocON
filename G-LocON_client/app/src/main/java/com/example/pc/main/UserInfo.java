package com.example.pc.main;

/*
 * Created by MF17037 on 2017/12/13.
 */

public class UserInfo {
    private String publicIP;
    private int publicPort;
    private String privateIP;
    private int privatePort;
    private Double latitude;
    private Double longitude;
    private String peerId;
    private double speed;

    public UserInfo() { }

    public UserInfo(String publicIP, int publicPort, String privateIP, int privatePort, Double latitude, Double longitude, String peerId, double speed) {
        this.publicIP = publicIP;
        this.publicPort = publicPort;
        this.privateIP = privateIP;
        this.privatePort = privatePort;
        this.latitude = latitude;
        this.longitude = longitude;
        this.peerId = peerId;
        this.speed = speed;
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

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
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

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setPeerId(String peerId){
        this.peerId = peerId;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

}

