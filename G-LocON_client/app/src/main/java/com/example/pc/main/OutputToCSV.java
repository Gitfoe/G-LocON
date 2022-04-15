package com.example.pc.main;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Shimomura on 2017/04/26.
 */

/**
 * CSVにデータを出力するクラス
 * @author Shimomura
 */

public class OutputToCSV {
    private File file;
    private FileWriter fw;
    private PrintWriter pw;


    public OutputToCSV(String fileName) {
        file = Environment.getExternalStorageDirectory();
        try {
            fw = new FileWriter(file.getPath() + fileName, false);//"/test.csv"
        } catch (IOException e) {
            e.printStackTrace();
        }
        pw = new PrintWriter(new BufferedWriter(fw),true);
    }

    public void setFieledName(String[] name) {
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
     * CSVファイルのフィールドを定義するメソッド
     * @param name
     *      エクセルのフィールド名
     */
    public void OutputFieledName(String... name) {
        for (int i = 0; i < name.length; i++) {
            pw.print(name[i]);
            pw.print(",");
        }
        pw.println();
    }

    /**
     * CSVファイルのフィールドを定義するメソッド
     * @param datas
     *      エクセルに出力したいデータ
     */
    public void OutputData(String... datas){
        for(int i = 0; i < datas.length; i++){
            pw.print(datas[i]);
            pw.print(",");
        }
        pw.println();
    }


    /*
    ファイル出力を終了する際にflushとcloseを行う
    分からない方はActivityを終了させる時にこのメソッドを呼ぶべき
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
