package com.example.pc.main;
import static java.lang.Math.toRadians;

/*
 * Created by MF17037 on 2018/08/04.
 * Refer to the website: http://www.serendip.ws/archives/5281
 * Class for obtaining the direction of the current terminal, one previous location information is required.
 */

public class HeadUp {
    private double latitude1;
    private double longitude1;
    private double latitude2;
    private double longitude2;

    HeadUp(double fromLat, double fromLng, double toLat, double toLng){
        this.latitude1 = toRadians(fromLat);
        this.longitude1 = toRadians(fromLng);
        this.latitude2 = toRadians(toLat);
        this.longitude2 = toRadians(toLng);
    }

    public double getNowAngle(){
        double Y = Math.sin(longitude2 - longitude1) * Math.cos(latitude2);
        double X = Math.cos(latitude1) * Math.sin(latitude2) - Math.sin(latitude1) * Math.cos(latitude2) * Math.cos(longitude2 - longitude1);
        double deg = Math.toDegrees(Math.atan2(Y, X));
        double angle = (deg + 360) % 360;
        return (Math.abs(angle) + (1 / 7200));
    }
}
