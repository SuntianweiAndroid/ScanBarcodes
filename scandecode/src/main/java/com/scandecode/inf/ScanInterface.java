package com.scandecode.inf;

/**
 * Created by lenovo-pc on 2017/6/29.
 */

public interface ScanInterface {

    interface OnScanListener {
        void getBarcode(String data);

        void getBarcodeByte(byte[] bytes);
    }

    /**
     * 初始扫描服务
     *
     * @param s 是否屏蔽快捷扫描按键
     */
    void initService(String s);

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
     *
     * @param scanListener
     */
    void getBarCode(OnScanListener scanListener);

    /**
     * 退出
     */
    void onDestroy();
}
