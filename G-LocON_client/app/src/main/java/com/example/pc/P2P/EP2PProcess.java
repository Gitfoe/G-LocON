package com.example.pc.P2P;

/*
 * Created by pc on 2018/06/09.
 * Processing division of P2P communication
 */

public enum EP2PProcess {
    NATRegisterDstUsers, // Register peripheral user information received from signaling servers to NAT
    NATRegisterSrcUser, // Register information on users who have searched for confidence in the NAT
    SendLocation, // Send data to peripheral users
    // Transmit ACK for data received
}
