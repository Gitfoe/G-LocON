package com.example.pc.main;

import android.graphics.Color;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

/*
 * Created by yusuke on 2016/09/20.
 */

public class CreateCircle {

    CreateCircle(){

    }

    public CircleOptions createCircleOptions(LatLng position, Double range){
        CircleOptions co = new CircleOptions();
        co.center(position);
        co.radius(range);
        co.fillColor(Color.parseColor("#3300FFCC")); // Color inside the circle
        co.strokeColor(Color.parseColor("#FF0000FF")); // Circle frame color
        co.strokeWidth(2); // Circle枠の太さ
        return co;
    }
}
