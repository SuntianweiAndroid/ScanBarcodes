package com.scandecode.inf;

/**
 * Created by lenovo-pc on 2017/6/29.
 */

public interface ScanInterface {

    public interface OnScanListener {
        public void getBarcode(String data);
    }

    /**
     * 初始扫描服务
     */
    void initService();
    /**
     * 打开扫描
     */
    void starScan();

    /**
     * 停止扫描
     */
    void stopScan();

    /**
     * 获取扫描解码结果
     * @param scanListener
     */
    void getBarCode(OnScanListener scanListener);

    /**
     * 退出
     */
    void onDestroy ();
}
