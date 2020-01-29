package com.example.news.network_status;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkConnectionStatus {
//    检测网络是否连接
    public static boolean checkNetworkConnection(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//        判断WiFi网络和移动网络是否可用
        if (wifi.isConnected() || mobile.isConnected())
        {
            return true;
        }else{
            return false;
        }
    }
}