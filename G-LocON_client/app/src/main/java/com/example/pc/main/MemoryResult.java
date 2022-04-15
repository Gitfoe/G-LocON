package com.example.pc.main;

import android.os.Environment;
import android.util.Log;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/*
 * Created by pc on 2018/08/12.
 */

public class MemoryResult {
    List<MemoryToSendData> sendData;
    List<MemoryToReceiveData> receiveData;
    SimpleDateFormat sdf;
    Date dateSend = null;
    Date dateReceive = null;
    private File file;
    private FileWriter fw;
    private PrintWriter pw;

    public MemoryResult(List<MemoryToSendData> sendData,List<MemoryToReceiveData> receiveData){
        this.sendData = sendData;
        this.receiveData = receiveData;
        sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        // OutputToCSV();
    }

    public void OutputToCSV() {
        UtilCommon utilCommon = (UtilCommon)UtilCommon.getAppContext();
        file = Environment.getExternalStorageDirectory();
        try {
            fw = new FileWriter(file.getPath()+"/"+ utilCommon.getPeerId()+"result.csv", false);//"/test.csv"
        } catch (IOException e) {
            e.printStackTrace();
        }
        pw = new PrintWriter(new BufferedWriter(fw),true);
        createResultFile();
    }

    public void createResultFile(){
        pw.print("LocationUpdateCount");
        pw.print(",");
        pw.print("EndPointIP");
        pw.print(",");
        pw.print("EndPointPort");
        pw.print(",");
        pw.print("RTT");
        pw.println();

        outputDataToResultFile();
    }

    public void outputDataToResultFile() {
        int all = 0;
        Log.d("MemoryResult","sendDataSize"+sendData.size());
        Log.d("MemoryResult","receiveDataSize"+receiveData.size());
        for(int j = 0; j < sendData.size(); j++) {
            MemoryToSendData sd = sendData.get(j);
            for(int k = 0; k < receiveData.size(); k++) {
                MemoryToReceiveData rd = receiveData.get(k);

                if(sd.getLocationUpdateCount().equals(rd.getLocationUpdateCount()) && sd.getEndPointIP().equals(rd.getEndPointIP())&& sd.getEndPointPort().equals(rd.getEndPointPort())) {

                    // Create the "Attachment".
                    try {
                        dateSend = sdf.parse(sd.getSendTime());
                        dateReceive = sdf.parse(rd.getReceiveTime());
                    } catch (ParseException e) {
                        Log.d("MemoryResult","Error:"+e);
                    }

                    // Convert a date to a long value.
                    long dateTimeSend = dateSend.getTime();
                    long dateTimeReceive = dateReceive.getTime();

                    // Calculate the RTT time.
                    long tuusinjikan = (dateTimeReceive-dateTimeSend);

                    pw.print(sd.getLocationUpdateCount());
                    pw.print(",");
                    pw.print(sd.getEndPointIP());
                    pw.print(",");
                    pw.print(sd.getEndPointPort());
                    pw.print(",");
                    pw.print(tuusinjikan);
                    pw.println();
                    all++;
                    break;
                }
            }
        }
        System.out.println("Completion");

        pw.flush();
        pw.close();
    }
}