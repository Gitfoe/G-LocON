package com.example.pc.main;

import java.util.Date;

import java.text.SimpleDateFormat;

/**
 * Created by minet-hp on 2017/03/20.
 */


public class SetDate {

    public String convertLong(long ntpTime){
        Date date = new Date(ntpTime);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        String tmp = sdf.format(date);
        return tmp;
    }
}