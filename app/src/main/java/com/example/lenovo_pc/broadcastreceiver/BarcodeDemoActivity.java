//package com.example.lenovo_pc.broadcastreceiver;
//
//import android.app.Activity;
//import android.content.BroadcastReceiver;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.SystemClock;
//import android.os.SystemProperties;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.CompoundButton;
//import android.widget.CompoundButton.OnCheckedChangeListener;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.ToggleButton;
//
//import com.android.barcode.R;
//
//public class BarcodeDemoActivity extends Activity implements OnClickListener {
//
//    private EditText mReception;
//    private Button btnSingleScan, btnClear;
//    private ToggleButton toggleButtonRepeat;
//    private boolean isFlag = false;
//    private int scancount=0;
//    //解码广播
//    private String RECE_DATA_ACTION = "com.se4500.onDecodeComplete";
//    //调用扫描广播
//    private String START_SCAN_ACTION = "com.geomobile.se4500barcode";
//    //停止扫描广播
//    private String STOP_SCAN = "com.geomobile.se4500barcodestop";
//    private TextView tvcound;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.barcode);
////        SystemProperties.set("persist.sys.iscamra","close");
////        Intent Barcodeintent = new Intent();
////        Barcodeintent.setPackage("com.geomobile.oemscanservice");
////        startService(Barcodeintent);
//        btnSingleScan = (Button) findViewById(R.id.buttonscan);
//        btnClear = (Button) findViewById(R.id.buttonclear);
//        toggleButtonRepeat = (ToggleButton) findViewById(R.id.button_repeat);
//        tvcound = (TextView) findViewById(R.id.tv_cound);
//        btnSingleScan.setOnClickListener(this);
//        btnClear.setOnClickListener(this);
//        toggleButtonRepeat.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//                if (isChecked) {
//                    isFlag = true;
//                    scancount=0;
//                    handler.removeCallbacks(startTask);
//                    handler.postDelayed(startTask, 0);
//                } else {
//                    isFlag = false;
//                    cancelRepeat();
//                }
//            }
//        });
//
//        mReception = (EditText) findViewById(R.id.EditTextReception);
//        //注册系统广播  解码扫描到的数据
//        IntentFilter iFilter = new IntentFilter();
//        iFilter.addAction(RECE_DATA_ACTION);
//        registerReceiver(receiver, iFilter);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        SystemProperties.set("persist.sys.scanstopimme", "false");
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.buttonclear:
//                mReception.setText("");
//                break;
//            case R.id.buttonscan:
//                startScan();
//                break;
//            default:
//                break;
//        }
//    }
//
//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//        public void onReceive(android.content.Context context,
//                              android.content.Intent intent) {
//            String action = intent.getAction();
//            if (action.equals(RECE_DATA_ACTION)) {
//                String data = intent.getStringExtra("se4500");
//                scancount+=1;
//                tvcound.setText("扫描次数："+scancount+"");
//                mReception.append(data + "\n");
//                if (isFlag) {
//                    cancelRepeat();
//                    Log.i(TAG, "decoder1");
////                    startScan();
//                    handler.postDelayed(startTask,0);
//                } else {
//                    Log.i(TAG, "decoder2");
//                    cancelRepeat();
//                }
//            }
//        }
//
//    };
//    String TAG = "task";
//    Handler handler = new Handler();
//
//    //连续扫描
//    private Runnable startTask = new Runnable() {
//        @Override
//        public void run() {
//            startScan();
//            handler.postDelayed(startTask, 3000);
//        }
//    };
//
//
//    /**
//     * 发送广播  调用系统扫描
//     */
//    private void startScan() {
//        Intent intent = new Intent();
//        intent.setAction(STOP_SCAN);
//        sendBroadcast(intent);
//        SystemProperties.set("persist.sys.scanstopimme", "true");
//        Log.i(TAG, "stop");
//        SystemClock.sleep(20);
//        Log.i(TAG, "start");
//        SystemProperties.set("persist.sys.scanstopimme", "false");
//        intent.setAction(START_SCAN_ACTION);
//        sendBroadcast(intent, null);
//    }
//
//    private void cancelRepeat() {
//        handler.removeCallbacks(startTask);
//        Intent intent = new Intent();
//        intent.setAction("com.geomobile.se4500barcodestop");
//        sendBroadcast(intent);
//        SystemProperties.set("persist.sys.scanstopimme", "true");
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        cancelRepeat();
//        handler.removeCallbacks(startTask);
//        unregisterReceiver(receiver);
//        super.onDestroy();
//    }
//
//}
