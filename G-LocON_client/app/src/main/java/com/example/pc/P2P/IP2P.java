package com.example.pc.P2P;

import com.example.pc.main.UserInfo;

import java.util.ArrayList;

/**
 * Created by pc on 2018/06/09.
 * このインターフェースはMainActivityでimplementし，P2Pで使用
 */

public interface IP2P {
    void onGetDetailUserInfo(UserInfo receiveUserInfo,ArrayList<UserInfo> userInfos); //周辺ユーザからデータが送られた場合
    void onGetPeripheralUsersInfo(ArrayList<UserInfo> userInfos);//シグナリングサーバから周辺ユーザ情報を取得した場合
}
