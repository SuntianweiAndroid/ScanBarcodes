package com.scandecode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.util.Log;

import com.scandecode.inf.ScanInterface;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static android.os.SystemProperties.get;

/**
 * Created by lenovo-pc on 2017/6/29.
 */

public class ScanDecode implements ScanInterface {
    //解码广播
    private static final String RECE_DATA_ACTION = "com.se4500.onDecodeComplete";
    //调用扫描广播
    private static final String START_SCAN_ACTION = "com.geomobile.se4500barcode";
    //停止扫描广播
    private static final String STOP_SCAN = "com.geomobile.se4500barcodestop";

    private static final String SERVICE_6603 = "com.geomobile.oemscanservice";
    private static final String SERVICE_ELSE = "com.geomobile.BarcodeService";//nl95\n43扫头
    private Context myContext;
    private String TAG = "scandecode";
    private OnScanListener listener;
    private boolean isFlag = false;
    private String scanmode = "";

    public ScanDecode(Context context) {
        this.myContext = context;
        //判断扫描的三种模式
        if (SystemProperties.get("persist.sys.scanmode", "one").equals("one")) {
            scanmode = "one";
            Log.i(TAG, "ScanDecode: " + scanmode);
        } else if (SystemProperties.get("persist.sys.scanmode", "one").equals("three")) {

            scanmode = "three";
            Log.i(TAG, "ScanDecode: " + scanmode);
        } else if (SystemProperties.get("persist.sys.scanmode", "one").equals("two")) {
            scanmode = "two";
            Log.i(TAG, "ScanDecode: " + scanmode);
        }
        SystemProperties.set("persist.sys.scanmode", "two");//默认设置模式二单次扫描
    }

    /**
     * 启动扫描服务
     *
     * @param s
     */
    private void startScanService(String s) {
        Intent Barcodeintent = new Intent();
        Barcodeintent.setPackage(s);
        myContext.startService(Barcodeintent);
    }

    /**
     * 停止扫描服务
     *
     * @param s
     */
    private void stopScanService(String s) {
        Intent Barcodeintent = new Intent();
        Barcodeintent.setPackage(s);
        myContext.stopService(Barcodeintent);
    }

    @Override
    public void initService() {
        if (SystemProperties.get("persist.sys.keyreport", "true").equals("false")) {//判断设置中快捷使能是否勾选
            isFlag = true;
            if (SystemProperties.get("persist.sys.scanheadtype").equals("6603")) {//判断是否为6603扫头
                startScanService(SERVICE_6603);
            } else {
                startScanService(SERVICE_ELSE);
            }
        }
        //注册显示decode
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(RECE_DATA_ACTION);
        myContext.registerReceiver(receiver, iFilter);
    }

    @Override
    public void starScan() {
        sendBroadcasts(STOP_SCAN);
        SystemProperties.set("persist.sys.scanstopimme", "true");//6603扫头特殊属性停止设置为true
        Log.i(TAG, "stop");
        SystemClock.sleep(20);
        Log.i(TAG, "start");
        SystemProperties.set("persist.sys.scanstopimme", "false");//6603扫头开始设置为false
        sendBroadcasts(START_SCAN_ACTION);
    }

    @Override
    public void stopScan() {
        if (get("persist.sys.scanheadtype").equals("6603")) {
            sendBroadcasts(STOP_SCAN);
            SystemProperties.set("persist.sys.scanstopimme", "true");
        } else {
            try {//n43-nl95 停止扫描
                android.os.SystemProperties.set("persist.sys.startscan", "false");
                File ScanDeviceName = new File("/proc/driver/scan");
                BufferedWriter ScanCtrlFileWrite = new BufferedWriter(new FileWriter(ScanDeviceName, false));
                ScanCtrlFileWrite.write("trigoff");
                ScanCtrlFileWrite.flush();
                ScanCtrlFileWrite.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 获取条码监听
     *
     * @param scanListener
     */
    @Override
    public void getBarCode(OnScanListener scanListener) {
        this.listener = scanListener;
    }

    /**
     * 回复系统扫描设置原始状态
     */
    @Override
    public void onDestroy() {
        myContext.unregisterReceiver(receiver);
        if (isFlag) {
            isFlag = false;
            if (SystemProperties.get("persist.sys.scanheadtype").equals("6603")) {
                stopScanService(SERVICE_6603);
            } else {
                stopScanService(SERVICE_ELSE);
            }
        }
        if (scanmode.equals("one")) {
            SystemProperties.set("persist.sys.scanmode", "one");
        } else if (scanmode.equals("two")) {
            SystemProperties.set("persist.sys.scanmode", "two");
        } else if (scanmode.equals("three")) {
            SystemProperties.set("persist.sys.scanmode", "three");
        } else {
            scanmode = "";
        }
    }


    private void sendBroadcasts(String s) {
        Intent intent = new Intent();
        intent.setAction(s);
        myContext.sendBroadcast(intent);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(android.content.Context context,
                              android.content.Intent intent) {
            String action = intent.getAction();
            if (action.equals(RECE_DATA_ACTION)) {
                String data = intent.getStringExtra("se4500");
                if (data != null) {
                    listener.getBarcode(data);
                }
            }
        }
    };
}
