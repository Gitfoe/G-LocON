package com.example.pc.P2P;

import android.location.Location;
import android.os.AsyncTask;

import com.example.pc.main.MemoryResult;
import com.example.pc.main.MemoryToReceiveData;
import com.example.pc.main.MemoryToSendData;
import com.example.pc.main.OutputToCSV;
import com.example.pc.main.SetDate;
import com.example.pc.main.UserInfo;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by pc on 2018/06/09.
 */

public class P2P implements IP2PReceiver {
    private IP2P iP2P;
    private DatagramSocket socket;
    private UserInfo myUserInfo;
    private ArrayList<UserInfo> peripheralUsers; //周辺ユーザ情報
    private OutputToCSV sendFileInput; //sendデータをCSVに書き込み
    private OutputToCSV receiveFileInput; //receiveデータをCSVに書き込み
    private List<MemoryToSendData> sendMemory; //sendデータを記録
    private List<MemoryToReceiveData> receiveMemory; //receiveデータを記録

    /**
     * デフォルトコンストラクタ
     * データをcsvに記録するためのsetUpMemoryを実行
     *
     * @param socket
     * @param myUserInfo
     * @param iP2P
     */
    public P2P(DatagramSocket socket, UserInfo myUserInfo, IP2P iP2P) {
        this.iP2P = iP2P;
        this.socket = socket;
        this.myUserInfo = myUserInfo;
        peripheralUsers = new ArrayList<>();

        setUpMemory();
    }

    public ArrayList<UserInfo> getPeripheralUsers(){
        return  peripheralUsers;
    }

    /**
     * 自身のユーザ情報の更新
     *
     * @param myUserInfo
     */
    public void setMyUserInfo(UserInfo myUserInfo) {
        this.myUserInfo = myUserInfo;
    }

    /**
     * Receiverの処理を非同期で実行
     */
    public void p2pReceiverStart() {
        P2PReceiver p2pReceiver = new P2PReceiver(socket, this);
        p2pReceiver.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    /**************SignalingServerへの接続系**************************/
    public void signalingRegister() {
        ESignalingProcess eSignalingProcess;
        eSignalingProcess = ESignalingProcess.REGISTER;
        Signaling signaling = new Signaling(socket, myUserInfo, eSignalingProcess);
        signaling.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void signalingUpdate() {
        ESignalingProcess eSignalingProcess;
        eSignalingProcess = ESignalingProcess.UPDATE;
        Signaling signaling = new Signaling(socket, myUserInfo, eSignalingProcess);
        signaling.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void signalingSearch(double searchDistance) {
        ESignalingProcess eSignalingProcess;
        eSignalingProcess = ESignalingProcess.SEARCH;
        Signaling signaling = new Signaling(socket, myUserInfo, searchDistance, eSignalingProcess);
        signaling.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void signalingDelete() {
        ESignalingProcess eSignalingProcess;
        eSignalingProcess = ESignalingProcess.DELETE;
        Signaling signaling = new Signaling(socket, myUserInfo, eSignalingProcess);
        signaling.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    /**************SignalingServerへの接続系**************************/


    /*****************************P2PSender系*************************/
    public void natRegisterDstUsers() {
        EP2PProcess eP2PProcess = EP2PProcess.NATRegisterDstUsers;
        P2PNatRegisterSender p2pNatRegisterSender = new P2PNatRegisterSender(socket, myUserInfo.getPublicIP(), myUserInfo.getPublicPort(), peripheralUsers, eP2PProcess);
        p2pNatRegisterSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void natRegisterSrcUser(UserInfo srcUser) {
        EP2PProcess eP2PProcess = EP2PProcess.NATRegisterSrcUser;
        P2PNatRegisterSender p2pNatRegisterSender = new P2PNatRegisterSender(socket, myUserInfo.getPublicIP(), myUserInfo.getPublicPort(), srcUser, eP2PProcess);
        p2pNatRegisterSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void sendLocation(int locationUpdateCount) {
        EP2PProcess eP2PProcess = EP2PProcess.SendLocation;
        P2PSender p2pSender = new P2PSender(socket, locationUpdateCount, myUserInfo, peripheralUsers, eP2PProcess);
        MemoryToCSV_Send(locationUpdateCount);
        p2pSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    /*
    public void sendAck(int locationCount, String srcIP, int srcPort) {
        EP2PProcess eP2PProcess = EP2PProcess.Ack;
        SendAck sendAck = new SendAck(socket, locationCount, srcIP, srcPort, eP2PProcess);
        sendAck.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

     */
    /*****************************P2PSender系*************************/



    /**
     * IP2PReceiverのレシーバ
     *
     * @param newPeripheralUsers 新しい周辺ユーザ情報
     */
    @Override
    public void onGetPeripheralUser(ArrayList<UserInfo> newPeripheralUsers) {
        peripheralUsers = newPeripheralUsers;
        iP2P.onGetPeripheralUsersInfo(peripheralUsers);
        natRegisterDstUsers();
    }


    /**
     * @param srcUserInfo 自信を検索したユーザ情報
     *                    シグナリングサーバに接続した送信元ユーザのIPとポートを自身のNATに記録する
     */

    @Override
    public void onDoUDPHolePunching(UserInfo srcUserInfo) {
        natRegisterSrcUser(srcUserInfo);

        for (int i = 0; i < peripheralUsers.size(); i++) {
            if (srcUserInfo.getPublicIP().equals(peripheralUsers.get(i).getPublicIP()) && srcUserInfo.getPublicPort() == peripheralUsers.get(i).getPublicPort() &&
                    srcUserInfo.getPrivateIP().equals(peripheralUsers.get(i).getPrivateIP()) && srcUserInfo.getPrivatePort() == peripheralUsers.get(i).getPrivatePort()) {
                return;
            }
        }
        peripheralUsers.add(srcUserInfo);
    }


    /**
     * ピアから直接受信したユーザ情報
     *
     * @param locationUpdateCount 相手が位置情報を更新した回数
     * @param srcIP               通信相手のIP
     * @param srcPort             通信相手のポート
     * @param location            相手の位置情報
     * @param peerId              相手のIPアドレス
     * @param speed               相手の速度
     */
    @Override
    public void onGetPeripheralUserLocation(int locationUpdateCount, String srcIP, int srcPort, Location location, String peerId, double speed) {
        for (int i = 0; i < peripheralUsers.size(); i++) {
            if (srcIP.equals(peripheralUsers.get(i).getPublicIP()) && srcPort == peripheralUsers.get(i).getPublicPort()) {
                peripheralUsers.get(i).setLatitude(location.getLatitude());
                peripheralUsers.get(i).setLongitude(location.getLongitude());
                peripheralUsers.get(i).setPeerId(peerId);
                peripheralUsers.get(i).setSpeed(speed);
                //sendAck(locationUpdateCount, srcIP, srcPort);
                iP2P.onGetDetailUserInfo(peripheralUsers.get(i), peripheralUsers);
                return;
            }
            if (srcIP.equals(peripheralUsers.get(i).getPrivateIP()) && srcPort == peripheralUsers.get(i).getPrivatePort()) {
                peripheralUsers.get(i).setLatitude(location.getLatitude());
                peripheralUsers.get(i).setLongitude(location.getLongitude());
                peripheralUsers.get(i).setPeerId(peerId);
                peripheralUsers.get(i).setSpeed(speed);
                //sendAck(locationUpdateCount, srcIP, srcPort);
                iP2P.onGetDetailUserInfo(peripheralUsers.get(i), peripheralUsers);
                return;
            }
        }
    }


    /**
     * 相手からのAckを受信
     *
     * @param locationCount 相手から転送された自身の位置情報更新回数
     * @param endPointIP    相手のIPアドレス
     * @param endPointPort  相手のポート番号
     */
    @Override
    public void onGetAck(int locationCount, String endPointIP, int endPointPort) {
        MemoryToCSV_Receive(locationCount, endPointIP, endPointPort);
    }


    /**
     * データをcsvに記録するためにフィールを設定
     */
    public void setUpMemory() {
        sendMemory = Collections.synchronizedList(new ArrayList<MemoryToSendData>());
        receiveMemory = Collections.synchronizedList(new ArrayList<MemoryToReceiveData>());
        sendFileInput = new OutputToCSV("/send.csv");//データ計測のファイル名
        String[] fieldName = {"LocationUpdateCount", "endPointIP", "endPointPort", "sendTime"};//CSVファイルのフィールド
        sendFileInput.setFieledName(fieldName);

        receiveFileInput = new OutputToCSV("/receive.csv");//同様
        String[] fieldName2 = {"LocationUpdateCount", "endPointIP", "endPointPort", "AckReceiveTime"};
        receiveFileInput.setFieledName(fieldName2);
    }


    /**
     * sendデータをリストに保管
     *
     * @param locationUpdateCount 自身が取得した位置情報更新回数
     */
    public void MemoryToCSV_Send(int locationUpdateCount) {
        String sendTime = "";
        SetDate d = new SetDate();
        sendTime = d.convertLong(System.currentTimeMillis());


        for (int i = 0; i < peripheralUsers.size(); i++) {
            if (!peripheralUsers.get(i).getPublicIP().equals(myUserInfo.getPublicIP())) {
                MemoryToSendData mtsd = new MemoryToSendData(String.valueOf(locationUpdateCount), peripheralUsers.get(i).getPublicIP(), String.valueOf(peripheralUsers.get(i).getPublicPort()), sendTime);
                sendMemory.add(mtsd);
            } else {
                MemoryToSendData mtsd = new MemoryToSendData(String.valueOf(locationUpdateCount), peripheralUsers.get(i).getPrivateIP(), String.valueOf(peripheralUsers.get(i).getPrivatePort()), sendTime);
                sendMemory.add(mtsd);
            }
        }
    }


    /**
     * receiveデータをリストに保管
     *
     * @param locationCount 自身の転送された位置情報取得回数
     * @param endPointIP    相手のIPアドレス
     * @param endPointPort  相手のポート番号
     */
    public void MemoryToCSV_Receive(int locationCount, String endPointIP, int endPointPort) {
        String receiveTime = "";
        SetDate d = new SetDate();
        try {
            receiveTime = d.convertLong(System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }

        MemoryToReceiveData mtrd = new MemoryToReceiveData(String.valueOf(locationCount), endPointIP, String.valueOf(endPointPort), receiveTime);
        receiveMemory.add(mtrd);
    }


    /**
     * sendデータのcsvを作成
     */
    public void fileInputMemorySendData() {
        for (int i = 0; i < sendMemory.size(); i++) {
            MemoryToSendData tmp = sendMemory.get(i);
            sendFileInput.OutputData(tmp.getLocationUpdateCount(), tmp.getEndPointIP(), tmp.getEndPointPort(), tmp.getSendTime());
        }
        sendMemory.clear();
        sendFileInput.fileClose();
    }


    /**
     * receiveデータのcsvデータを作成
     */
    public void fileInputMemoryReceiveData() {
        for (int i = 0; i < receiveMemory.size(); i++) {
            MemoryToReceiveData tmp = receiveMemory.get(i);
            receiveFileInput.OutputData(tmp.getLocationUpdateCount(), tmp.getEndPointIP(), tmp.getEndPointPort(), tmp.getReceiveTime());
        }
        receiveMemory.clear();
        receiveFileInput.fileClose();
    }

    public void fileInputMemoryResult(){
        MemoryResult memoryResult = new MemoryResult(sendMemory,receiveMemory);
        memoryResult.OutputToCSV();
    }
}
