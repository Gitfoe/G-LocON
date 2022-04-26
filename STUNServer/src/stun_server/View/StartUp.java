package stun_server.View;

import stun_server.Controller.STUNServer;
import stun_server.Model.UserInfo;

import java.net.DatagramSocket;
import java.net.SocketException;

public class StartUp {

    final static int SERVER_PORT = 55554; // Port number

    public static void main(String[] args) {

        // Object creation
        DatagramSocket serverSocket;
        UserInfo userInfo = new UserInfo();

        try {
            // Socket generation
            serverSocket = new DatagramSocket(SERVER_PORT);

            // STUN server object generation
            STUNServer stunServer = new STUNServer(serverSocket, userInfo);

            // Start of asynchronous processing (STUN server)
            stunServer.start();

            System.out.println("The STUN server started on port " + SERVER_PORT);

        } catch (SocketException e) {
            System.out.println(e.getMessage());
        }
    }
}