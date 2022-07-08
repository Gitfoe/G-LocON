package com.example.pc.P2P;

import com.example.pc.main.UserInfo;

import java.util.ArrayList;

/*
 * Created by pc on 2018/06/09.
 * This interface is implemented in MainActivity and used in P2P
 */

public interface IP2P {
    void onGetDetailUserInfo(UserInfo receiveUserInfo,ArrayList<UserInfo> userInfos); // When data is sent from a peripheral user
    void onGetPeripheralUsersInfo(ArrayList<UserInfo> userInfos);// When peripheral user information is acquired from a signaling server
}
