package com.example.pc.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import android.app.Application;
import android.content.Context;

/*
 * Created by Shimomura on 2017/04/26.
 */

/**
 * Class to output data to CSV
 * @author Shimomura
 */
public class OutputToCSV {
    private File file;
    private FileWriter fw;
    public PrintWriter pw;
    private Application application;

    public OutputToCSV(String fileName) {
        Context context = UtilCommon.getAppContext();
        file = new File(context.getExternalFilesDir(null), fileName);

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //file = android.os.Environment.getExternalStorageDirectory();
        try {
            fw = new FileWriter(file, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        pw = new PrintWriter(new BufferedWriter(fw), true);
    }

    public void setFieldName(String[] name) {
        for (int i = 0; i < name.length; i++) {
            pw.print(name[i]);
            if (i != name.length)
                pw.print(",");
        }
        pw.println();
    }

    public void setSendData(String sendDataCount, String locationGetTime, String sendTime, String peerId, String peerListUpdate){
        synchronized(pw) {
            pw.print(sendDataCount);
            pw.print(",");
            pw.print(locationGetTime);
            pw.print(",");
            pw.print(sendTime);
            pw.print(",");
            pw.print(peerId);
            pw.print(",");
            pw.print(peerListUpdate);
            //pw.flush();
            pw.println();
        }
    }

    public void setRecieveData(String sendDataCount,String RecieveTime, String senderPeerId){
        synchronized(pw) {
            pw.print(sendDataCount);
            pw.print(",");
            pw.print(RecieveTime);
            pw.print(",");
            pw.print(senderPeerId);
            pw.println();
        }
    }

    /**
     * Methods to define fields in a CSV file
     * @param name Excel field name
     */
    public void OutputFieldName(String... name) {
        for (int i = 0; i < name.length; i++) {
            pw.print(name[i]);
            pw.print(",");
        }
        pw.println();
    }

    /**
     * Methods to define fields in a CSV file
     * @param datas Data to be output to Excel
     */
    public void OutputData(String... datas){
        for(int i = 0; i < datas.length; i++){
            pw.print(datas[i]);
            pw.print(",");
        }
        pw.println();
    }

    /**
     Flush and close when terminating file output
     If you don't know, you should call this method when you exit Activity
     */
    public void fileClose(){
        pw.flush();

        try {
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        pw.close();
        try {
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
