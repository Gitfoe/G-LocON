package com.example.pc.P2P;

/**
 * Created by pc on 2018/06/09.
 * P2P通信の処理分け
 */

public enum EP2PProcess {
    NATRegisterDstUsers, //シグナリングサーバから受信した周辺ユーザ情報をNATに登録
    NATRegisterSrcUser, //自信を検索したユーザの情報をNATに登録
    SendLocation, //データを周辺ユーザに送信
   // Ack //データ受信に対するACKの送信
}
