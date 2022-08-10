package com.example.pc.main;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.pc.P2P.IP2P;
import com.example.pc.P2P.P2P;
import com.example.pc.STUNServerClient.ISTUNServerClient;
import com.example.pc.STUNServerClient.STUNServerClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Created by mf17037 shimomura on 2018/08/11.
 */

/**
 * Basic form of V2V
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationListener, OnMapReadyCallback, ISTUNServerClient, IP2P, IThrowListener{
    private EditText peerId;
    private Button start;
    private Button end;
    private Button plus;
    private Button minus;
    private Button angle;
    private Switch li_switch;
    private GoogleMap mMap;
    private Location mLocation; // Current location
    private float nowCameraAngle = 0; // Current camera angle
    private double nowSpeed = 0; // Current speed
    private List<MarkerInfo> markerList;
    private Circle circle = null; // Range of communication
    final static private double TOLERANCE_SPEED = 7; // Speed differential
    private float cameraLevel = 18.0f;
    final static private String HEAD_UP= "HEAD_UP";
    final static private String NORTH_UP= "NORTH_UP";
    private String cameraAngle = HEAD_UP;

    UtilCommon utilCommon; // Common data such as global IP of the server
    UserInfo myUserInfo; // Own information
    UserSettings myUserSettings; // Own settings
    private DatagramSocket socket;
    final static int NAT_TRAVEL_OK = 1; // OK if you have already obtained your own NAT converted information
    private int natTravel = 0; // Support for NAT_TRAVEL_OK
    final private static int USER_INFO_UPDATE_INTERVAL = 5; // Frequency of connecting to the signaling server (every 5 times location information is acquired)
    private int geoUpdateCount = 4; // Support for USER_INFO_UPDATE_INTERVAL
    private int totalGeoUpdateCount = 0; // Number of location updates
    private double searchRange = 100; // In meters
    private int addMarker = 0;
    final private int ADD_MARKER_PROGRESS = 1;
    final private int NOT_ADD_MARKER_PROGRESS = 0;
    private P2P p2p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        end = (Button)findViewById(R.id.end);
        plus = (Button)findViewById(R.id.plus);
        minus = (Button)findViewById(R.id.minus);
        angle = (Button)findViewById(R.id.angle);
        li_switch = (Switch)findViewById(R.id.li_switch);
        end.setOnClickListener(this);
        end.setVisibility(View.INVISIBLE);
        li_switch.setVisibility(View.INVISIBLE);
        peerId = (EditText) findViewById(R.id.peerId);
        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(this);


        createDatagramSocket();
        utilCommon = (UtilCommon) getApplication();
        myUserInfo = new UserInfo();
        // markerList = new CopyOnWriteArrayList<>();
        markerList = new ArrayList<>();


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapFragment);

        mapFragment.getMapAsync(this);
    }

    /**
     * Socket Generation Methods
     */
    private void createDatagramSocket() {
        try {
            socket = new DatagramSocket();
            socket.setReuseAddress(true);
        } catch (Exception e) {
        }
    }


    /**
     * Event listener called when button is pressed
     * Triggers a connection to the STUN server
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (R.id.start == v.getId()) {
            utilCommon.setSignalingServerIP("172.16.8.29"); // Server IP address
            utilCommon.setSignalingServerPort(55555); // Server port number
            utilCommon.setStunServerIP("172.16.8.29");  // Server IP address
            utilCommon.setStunServerPort(55554); // Server port number
            utilCommon.setPeerId(peerId.getText().toString());
            peerId.setVisibility(View.INVISIBLE);
            start.setVisibility(View.INVISIBLE);
            end.setVisibility(View.VISIBLE);
            li_switch.setVisibility(View.VISIBLE);

            plus.setOnClickListener(this);
            minus.setOnClickListener(this);
            angle.setOnClickListener(this);
            li_switch.setOnClickListener(this);

            mLocation = new Location("");
            //mLocation.setLatitude(35.951003); // In front of the university
            //mLocation.setLongitude(139.655367);// In front of the university
            mLocation.setLatitude(35.409716);// In front of the house
            mLocation.setLongitude(139.588568);// In front of the house

            STUNServerClient stunServerClient = new STUNServerClient(socket, this);
            stunServerClient.stunServerClientStart();
        }

        else if (R.id.end == v.getId()) {
            p2p.fileInputMemoryResult();
            p2p.fileInputMemorySendData();
            p2p.fileInputMemoryReceiveData();
            p2p.signalingDelete();
            System.exit(1);
        }

        else if (R.id.plus == v.getId()) {
            cameraLevel = cameraLevel + 1f;
        }

        else if (R.id.minus == v.getId()) {
            cameraLevel = cameraLevel - 1f;
        }

        else if (R.id.angle == v.getId()) {
            if(cameraAngle.equals(HEAD_UP)){
                cameraAngle = NORTH_UP;
                angle.setText("NORTHUP");
            }
            else if(cameraAngle.equals(NORTH_UP)){
                cameraAngle = HEAD_UP;
                angle.setText("HEADUP");
            }
        }

        else if (R.id.li_switch == v.getId()) {
            myUserSettings.setLi_enabled(li_switch.isChecked());
            p2p.signalingSettings(myUserSettings);
        }
    }

    /**
     * Event listener called when global IP and port number are obtained from STUN server
     * Triggers the execution of the location acquisition class.
     *
     * @param IP   NAT-transformed global IP
     * @param port NAT-transformed global PORT
     */
    @Override
    public void onGetGlobalIP_Port(String IP, int port) {
        myUserInfo.setPublicIP(IP);
        myUserInfo.setPublicPort(port);
        myUserInfo.setPrivateIP(GetPrivateIP());
        myUserInfo.setPrivatePort(socket.getLocalPort());
        myUserInfo.setPeerId(utilCommon.getPeerId());
        myUserInfo.setSpeed(nowSpeed);
        myUserInfo.setPeerId(utilCommon.getPeerId());
        myUserInfo.setLatitude(35.409716);
        myUserInfo.setLongitude(139.588568);

        p2p = new P2P(socket, myUserInfo, this);
        p2p.addThrowListener(this); // Subscribe to the retrieval of user settings event

        natTravel = NAT_TRAVEL_OK;
        p2p.p2pReceiverStart();
        p2p.signalingRegister();

        MyLocation myLocation = new MyLocation(this, this, 1);
        myLocation.createGoogleApiClient();
    }

    /**
     * Event listener called when map is available
     *
     * @param googleMap Mapped variable
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {

        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String tapPeerPublicIP = null;
                Integer tapPeerPublicPort = null;
                String tapPeerPrivateIP = null;
                Integer tapPeerPrivatePort = null;
                UserInfo tapPeer = null;
                for(int i = 0; i < markerList.size(); i++){
                    if(marker.equals(markerList.get(i).getMarker())){
                        tapPeerPublicIP = markerList.get(i).getPublicIP();
                        tapPeerPublicPort = markerList.get(i).getPublicPort();
                        tapPeerPublicIP = markerList.get(i).getPrivateIP();
                        tapPeerPublicPort = markerList.get(i).getPrivatePort();
                        break;
                    }
                }

                for(int i = 0; i < p2p.getPeripheralUsers().size(); i++){
                    if(markerList.get(i).getPublicIP().equals(p2p.getPeripheralUsers().get(i).getPublicIP()) &&
                            markerList.get(i).getPublicPort() == (p2p.getPeripheralUsers().get(i).getPublicPort()) &&
                            markerList.get(i).getPrivateIP().equals(p2p.getPeripheralUsers().get(i).getPrivateIP()) &&
                            markerList.get(i).getPrivatePort() == (p2p.getPeripheralUsers().get(i).getPrivatePort())){
                        tapPeer = p2p.getPeripheralUsers().get(i);
                        break;
                    }
                }

                String msg = "name:"+tapPeer.getPeerId()+"\nlatitude:"+tapPeer.getLatitude()+"\nlongitude:"+tapPeer.getLongitude()+"\nspeed:"+tapPeer.getSpeed();
                displayToast(msg);

                return false;
            }
        });
    }

    /**
     * Event listener called when acquiring location information
     * This event is used to send data to peers and signaling servers and to operate the camera.
     *
     * @param geo Current location
     */
    @Override
    public void onLocationChanged(Location geo) {
        double nowAngle = new HeadUp(mLocation.getLatitude(), mLocation.getLongitude(), geo.getLatitude(), geo.getLongitude()).getNowAngle();
        // Convert m/s to km/h
        nowSpeed = (new HubenyDistance().calcDistance(mLocation.getLatitude(), mLocation.getLongitude(), geo.getLatitude(), geo.getLongitude())) * 3.6;
        Log.d("log", "angle:" + nowAngle);
        Log.d("log", "speed:" + nowSpeed);
        mLocation = geo;
        cameraPosition(nowAngle);

        myUserInfo.setLatitude(geo.getLatitude());
        myUserInfo.setLongitude(geo.getLongitude());
        myUserInfo.setSpeed(nowSpeed);
        if (natTravel == NAT_TRAVEL_OK) {
            p2p.setMyUserInfo(myUserInfo);
            totalGeoUpdateCount++;
            geoUpdateCount++;

            if (geoUpdateCount == USER_INFO_UPDATE_INTERVAL) {
                geoUpdateCount = 0;
                p2p.signalingUpdate();
                p2p.signalingSearch(searchRange);
            }
            p2p.sendLocation(totalGeoUpdateCount);
        }
    }

    /**
     * Camera position on the map and manipulation of angles
     *
     * @param angle // Current terminal direction (north is 0 degrees)
     */
    public void cameraPosition(double angle) {
        if(cameraAngle.equals(HEAD_UP)) {
            if (Math.abs(nowCameraAngle - (float) angle) > 5)
                nowCameraAngle = (float) angle;
        }
        else if(cameraAngle.equals(NORTH_UP)){
            nowCameraAngle = 0;
        }

        LatLng location = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        CameraPosition cameraPos = new CameraPosition.Builder().target(location).zoom(cameraLevel).bearing(nowCameraAngle).tilt(60).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPos));
        if (circle == null) {
            circle = mMap.addCircle(new CreateCircle().createCircleOptions(location, searchRange)); // Show search radius circle
        } else {
            circle.setCenter(location);
        }
    }


    /**
     * Get the private IP of the terminal
     * @return Private IP address
     */
    public String GetPrivateIP() {
        String privateIP = null;
        try {
            for (NetworkInterface n : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                for (InetAddress addr : Collections.list(n.getInetAddresses())) {
                    if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
                        privateIP = addr.getHostAddress();
                        return privateIP;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return privateIP;
    }

    /**
     * Obtain detailed position and velocity information from the receiving party
     * Call marker operation
     *
     * @param userInfo            Information on communication partners
     * @param peripheralUserInfos Current number of users in proximity
     */
    @Override
    public void onGetDetailUserInfo(final UserInfo userInfo, ArrayList<UserInfo> peripheralUserInfos) {
        arrangeMarker(userInfo, peripheralUserInfos);
    }

    /**
     * Obtain peripheral user information from signaling server
     * Call marker operation
     *
     * @param peripheralUserInfos Peripheral User Information
     */
    @Override
    public void onGetPeripheralUsersInfo(ArrayList<UserInfo> peripheralUserInfos) {
        arrangeMarker(null, peripheralUserInfos);
    }

    /**
     * Control markers on the map (add, update, delete markers)
     *
     * @param userInfo            Communication partner information to update the marker position
     * @param peripheralUserInfos Current number of users in the vicinity
     */
    synchronized public void arrangeMarker(final UserInfo userInfo, final ArrayList<UserInfo> peripheralUserInfos) {
        //region Marker removal
        if (userInfo == null) {
            Log.d("Main_arrangeMarker", "Number of markers when the function to delete markers is called:" + markerList.size());
            Log.d("Main_arrangeMarker", "Current number of users in the vicinity:" + peripheralUserInfos.size());
            final ArrayList<MarkerInfo> removeMarker = new ArrayList<>(); // Stores markers to be deleted
            final ArrayList<MarkerInfo> continueMarker = new ArrayList<>();

            //region Search for markers to be deleted
            for (int i = 0; i < markerList.size(); i++) {
                int j = 0;
                for (; j < peripheralUserInfos.size(); j++) {
                    if (markerList.get(i).getPublicIP().equals(peripheralUserInfos.get(j).getPublicIP()) &&
                            markerList.get(i).getPublicPort() == (peripheralUserInfos.get(j).getPublicPort()) &&
                            markerList.get(i).getPrivateIP().equals(peripheralUserInfos.get(j).getPrivateIP()) &&
                            markerList.get(i).getPrivatePort() == (peripheralUserInfos.get(j).getPrivatePort())) {
                        continueMarker.add(markerList.get(i));
                        break;
                    }
                }
                if (j == peripheralUserInfos.size()) {
                    removeMarker.add(markerList.get(i));
                }
            }
            //endregion

            //region Perform marker deletion
            runOnUiThread(new Runnable() {
                public void run() {
                    Log.d("Main_arrangeMarker", "Number of markers immediately before the marker is deleted: 1" + markerList.size());
                    Log.d("Main_arrangeMarker", "Number of markers to be deleted: 1" + removeMarker.size());
                    if (peripheralUserInfos.size() == 0) {
                        for (int i = 0; i < markerList.size(); i++) {
                            markerList.get(i).getMarker().remove();
                        }
                        markerList.clear();
                    } else {
                        for (int i = 0; i < removeMarker.size(); i++) {
                            removeMarker.get(i).getMarker().remove();
                        }
                        markerList = continueMarker;
                    }
                }
            });
            return;
            //endregion
        }
        //endregion


        //region Create and update markers
        Log.d("Main_arrangeMarker", "Number of markers when performing a marker move" + markerList.size());
        waitUntilFinishAddMarker(); // If marker is being created in another thread, wait for it to finish

        //region Perform marker updates
        for (int i = 0; i < markerList.size(); i++) {
            final int tmp = i;
            if (markerList.get(i).getPublicIP().equals(userInfo.getPublicIP()) && markerList.get(i).getPublicPort() == (userInfo.getPublicPort()) &&
                    markerList.get(i).getPrivateIP().equals(userInfo.getPrivateIP()) && markerList.get(i).getPrivatePort() == (userInfo.getPrivatePort())) { // Boil out
                if (userInfo.getSpeed() - myUserInfo.getSpeed() > TOLERANCE_SPEED) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            markerList.get(tmp).getMarker().setPosition(new LatLng(userInfo.getLatitude(), userInfo.getLongitude()));
                            markerList.get(tmp).getMarker().setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            markerList.get(tmp).getMarker().setPosition(new LatLng(userInfo.getLatitude(), userInfo.getLongitude()));
                            System.out.println("Latitude"+userInfo.getLatitude()+"Longitude"+userInfo.getLongitude());
                            markerList.get(tmp).getMarker().setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            System.out.println("Marker set up complete");
                        }
                    });
                }
                return;
            }
        }
        //endregion


        //region Execute marker creation
        addMarker = ADD_MARKER_PROGRESS;
        runOnUiThread(new Runnable() {
            public void run() {
                Marker setMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(userInfo.getLatitude(), userInfo.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                markerList.add(new MarkerInfo(setMarker, userInfo.getPublicIP(), userInfo.getPublicPort(), userInfo.getPrivateIP(), userInfo.getPrivatePort()));
                addMarker = NOT_ADD_MARKER_PROGRESS;
            }
        });
        //endregion
    }
    //endregion

    private void waitUntilFinishAddMarker() {
        do {
            if (addMarker == NOT_ADD_MARKER_PROGRESS) {
                return;
            }
            try {
                Thread.sleep(100); // Sleep for 100 milliseconds
            } catch (InterruptedException e) {
            }
        } while (true);
    }

    private void displayToast(final String msg) {
        Handler handler = new Handler(getApplication().getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplication(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void Catch(UserSettings userSettings) {
        myUserSettings = userSettings; // Set user settings for later usage in this class
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                li_switch.setChecked(userSettings.isLi_enabled());
            }
        });
    }
}