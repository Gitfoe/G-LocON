package com.example.pc.P2P;

import android.location.Location;
import android.os.AsyncTask;

import com.example.pc.main.MemoryResult;
import com.example.pc.main.MemoryToReceiveData;
import com.example.pc.main.MemoryToSendData;
import com.example.pc.main.OutputToCSV;
import com.example.pc.main.SetDate;
import com.example.pc.main.UserInfo;
import com.example.pc.main.UtilCommon;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Created by pc on 2018/06/09.
 */

public class P2P implements IP2PReceiver {
    private IP2P iP2P;
    private DatagramSocket socket;
    private UserInfo myUserInfo;
    private ArrayList<UserInfo> peripheralUsers; // Perhiperal user information
    private OutputToCSV sendFileInput; // Write send data to CSV
    private OutputToCSV receiveFileInput; // Write receive data to CSV
    private List<MemoryToSendData> sendMemory; // Record send data
    private List<MemoryToReceiveData> receiveMemory; // Record receive data

    /**
     * Default constructor
     * Run setUpMemory to record data to csv
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
     * Update own user information
     */
    public void setMyUserInfo(UserInfo myUserInfo) {
        this.myUserInfo = myUserInfo;
    }

    /**
     * Asynchronous execution of Receiver processing
     */
    public void p2pReceiverStart() {
        P2PReceiver p2pReceiver = new P2PReceiver(socket, this);
        p2pReceiver.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    //region Connection system to SignalingServer
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
    //endregion

    //region P2P sender system
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
    //endregion

    /**
     * IP2P receiver
     * @param newPeripheralUsers New peripheral user information
     */
    @Override
    public void onGetPeripheralUser(ArrayList<UserInfo> newPeripheralUsers) {
        peripheralUsers = newPeripheralUsers;
        iP2P.onGetPeripheralUsersInfo(peripheralUsers);
        natRegisterDstUsers();
    }

    /**
     * @param srcUserInfo User information for confidence searches.
     *                    Record the IP and port of the source user connecting to the signaling server in its own NAT
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
     * User information received directly from peer
     * @param locationUpdateCount   Number of times the peer has updated its location information.
     * @param srcIP                 IP of the communication partner
     * @param srcPort               Port of the communication partner
     * @param location              Location information of the peer
     * @param peerId                IP address of the peer
     * @param speed                 Speed of the peer
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
     * Receive Ack from the other party
     * @param locationCount Number of own location updates forwarded by the peer
     * @param endPointIP IP address of the other party
     * @param endPointPort Port number of the other party
     */
    @Override
    public void onGetAck(int locationCount, String endPointIP, int endPointPort) {
        MemoryToCSV_Receive(locationCount, endPointIP, endPointPort);
    }

    /**
     * Set feeler to record data to CSV
     */
    public void setUpMemory() {
        sendMemory = Collections.synchronizedList(new ArrayList<MemoryToSendData>());
        receiveMemory = Collections.synchronizedList(new ArrayList<MemoryToReceiveData>());
        sendFileInput = new OutputToCSV("send.csv"); // File name for data measurement
        String[] fieldName = {"LocationUpdateCount", "endPointIP", "endPointPort", "sendTime"}; // CSV file fields
        sendFileInput.setFieldName(fieldName);

        receiveFileInput = new OutputToCSV("receive.csv"); // Likewise
        String[] fieldName2 = {"LocationUpdateCount", "endPointIP", "endPointPort", "AckReceiveTime"};
        receiveFileInput.setFieldName(fieldName2);
    }

    /**
     * Stores send data in a list
     * @param locationUpdateCount Number of times the location information has been updated that has been obtained by the user
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
     * Store received data in a list
     * @param locationCount Number of times own forwarded location information is acquired
     * @param endPointIP IP address of the other party
     * @param endPointPort Port number of the other party
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
     * Create CSV of sent data
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
     * Create CSV of received data
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
        if (sendMemory != null && receiveMemory != null)
        {
            UtilCommon utilCommon = (UtilCommon)UtilCommon.getAppContext();
            MemoryResult memoryResult = new MemoryResult(sendMemory,receiveMemory);
            OutputToCSV outputToCSV = new OutputToCSV(utilCommon.getPeerId() + "-result.csv"); // File name for data measurement
            memoryResult.createResultFile(outputToCSV.pw);
        }
    }
}
