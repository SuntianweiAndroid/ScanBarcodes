package com.example.lenovo_pc.broadcastreceiver;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.barcode.R;
import com.scandecode.ScanDecode;
import com.scandecode.inf.ScanInterface;

/**
 * Created by lenovo-pc on 2017/6/29.
 */

public class DecodeAct extends Activity {
    private EditText mReception;
    private Button btnSingleScan, btnClear;
    private ScanInterface scanDecode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        btnSingleScan = (Button) findViewById(R.id.buttonscan);
        scanDecode = new ScanDecode(this);
        scanDecode.initService("true");

//        SystemClock.sleep(2000);
        mReception = (EditText) findViewById(R.id.EditTextReception);
        btnSingleScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanDecode.starScan();
            }
        });
        btnClear = (Button) findViewById(R.id.buttonclear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanDecode.stopScan();
            }
        });

        scanDecode.getBarCode(new ScanInterface.OnScanListener() {
            @Override
            public void getBarcode(String data) {
                mReception.append(data+"\n");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scanDecode.onDestroy();
    }
}
