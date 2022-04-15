package com.example.pc.main;

/*
 * Created by MF17037 on 2018/08/04.
 * Class to measure distance traveled in meters
 * Coordinates before moving are required
 */

public class HubenyDistance {

    // Bounded-value system
    private static final double GRS80_A = 6378137.000; // Long radius a(m)
    private static final double GRS80_E2 = 0.00669438002301188; // First centrifugal rate e squared

    private double deg2rad(double deg){
        return deg * Math.PI / 180.0;
    }

    public double calcDistance(double lat1, double lng1, double lat2, double lng2){
        double my = deg2rad((lat1 + lat2) / 2.0); // Mean value of latitude
        double dy = deg2rad(lat1 - lat2); // Difference in latitude
        double dx = deg2rad(lng1 - lng2); // Difference in degrees

        // Find the radius of curvature of the Uyuden line (radius of the line connecting east and west)
        double sinMy = Math.sin(my);
        double w = Math.sqrt(1.0 - GRS80_E2 * sinMy * sinMy);
        double n = GRS80_A / w;

        // Find the radius of the meridian curve (radius of the line connecting north and south)
        double mnum = GRS80_A * (1 - GRS80_E2);
        double m = mnum / (w * w * w);

        // Hübeni's formula
        double dym = dy * m;
        double dxncos = dx * n * Math.cos(my);
        return Math.sqrt(dym * dym + dxncos * dxncos);
    }
}