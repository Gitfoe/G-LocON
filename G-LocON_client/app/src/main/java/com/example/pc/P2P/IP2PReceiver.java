package com.example.pc.P2P;
import android.location.Location;

import com.example.pc.main.UserInfo;
import com.example.pc.main.UserSettings;

import java.util.ArrayList;

/*
 * Created by pc on 2018/06/09.
 * This interface is implemented in P2P and used by P2PReceiver
 */

public interface IP2PReceiver {
    void onGetPeripheralUser(ArrayList<UserInfo> peripheralUsers); // When acquiring peripheral user information from the signaling server
    void onDoUDPHolePunching(UserInfo srcUser); // When registering user information that has searched for itself to the NAT
    void onGetPeripheralUserLocation(int locationUpdateCount,String srcIP,int srcPort,Location location,String peerId,double speed); // When getting data from peers
    void onGetSrcUserSettings(UserSettings userSettings); // When obtaining updated user settings
    void onGetAck(int locationCount, String endPointIP, int endPointPort); // When sending ACK
}
