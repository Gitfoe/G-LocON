package com.example.pc.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.LocationListener;


/**
使い方
 1.Activity側でパーミッションの確認をする
 2.パーミッション確認後、このクラスのインスタンスを生成
 ->Activity側でcom.google.android.gms.location.LocationListenerのインスタンスを引数にするがこれをimplementしthisを引数にする
 3.creategoogleApiClientを呼ぶ，すると位置情報を取得する
 4.コールバック関数を用いているので位置情報を取得したらActivity側に値を自動で返す
 ->onLocationChangedをActivity側で定義する必要がある

 よくわからない方は調べて
 */

public class MyLocation implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient googleApiClient;
    private Context context;
    private com.google.android.gms.location.LocationListener locationListener = null;
    private long setLocationUpdateInterval;

    /**
     * ContextとActivity側のcom.google.android.gms.location.LocationListenerを記録する
     * @param context ActivityのContext
     * @param locationListener Activityのcom.google.android.gms.location.LocationListener
     */
    MyLocation(Context context, com.google.android.gms.location.LocationListener locationListener, long setLocationUpdateInterval) {
        this.context = context;
        this.locationListener = locationListener;
        this.setLocationUpdateInterval = setLocationUpdateInterval;
    }








    //位置情報を取得するのに必要インスタンスとなるものを生成するメソッド
    public void createGoogleApiClient(){
        // GoogleApiClientの作成
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        googleApiClient.connect();
    }

//つながる場合はこのメソッドがコールバックされる
    @Override
    public void onConnected(Bundle connectionHint) {
        // Wearable.MessageApi.addListenerなどよぶ
        createLocationRequest();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // 切断された時の処理
        // Wearable.MessageApi.removeListenerなどよぶ
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // エラー処理
        // Google Play Servicesがインストールされていない場合などの案内を表示
    }

    /**
     * 現在地が更新された際のリスナーを設定
     *
     */
    private void createLocationRequest() {
        // networkではどんなに早くても5秒間隔で更新される
        // GPS利用時では設定した間隔で更新される
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(setLocationUpdateInterval);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // 設定したLocationRequestで位置情報の更新を開始
        if (context.getPackageManager().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
            // 設定したLocationRequestで位置情報の更新を開始
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, locationListener);
        }
    }


    /**
     * 位置情報の取得を終了する
     */
    public void stopGetLocation(){
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,locationListener);
    }
}