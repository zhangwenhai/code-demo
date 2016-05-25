/*
 * 文件名: ConnectionChangedReceiver.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 监听系统网络
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.component.service.receiver;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.android.common.FusionMessageType;
import com.android.component.log.Logger;
import com.android.common.FusionAction.BaseServiceAction;

/**
 * 监听系统网络<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class ConnectionChangedReceiver extends BroadcastReceiver
{
    
    /**
     * 日志TAG
     */
    private static final String TAG = "ConnectionChangedReceiver";
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (null == intent)
        {
            return;
        }
        String action = intent.getAction();
        Logger.i(TAG, "onReceive : " + action);
        
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)
                || Intent.ACTION_TIME_CHANGED.equals(action)
                || Intent.ACTION_DATE_CHANGED.equals(action)
                || Intent.ACTION_TIMEZONE_CHANGED.equals(action)
                || Intent.ACTION_TIME_TICK.equals(action))
        {
            //检查Service状态 
            if (!isServiceRunning(context))
            {
                Logger.v(TAG, "ConnectionChangedReceiver! start service"
                        + intent.getAction());
                //网络状态 serivceIntent.putExtra(ServiceAction.EXTRA_CONNECTIVITY,checkNetStatus(context));
                Intent intent1 = new Intent(BaseServiceAction.ACTION);
                context.startService(intent1);
            }
        }
    }
    
    /**
     * 检查Service状态<BR>
     * @param context Context
     * @return 检查Service状态
     */
    private boolean isServiceRunning(Context context)
    {
        boolean isServiceRunning = false;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if ("com.paitao.basic.android.component.service.core.BaseService".equals(service.service.getClassName()))
            {
                isServiceRunning = true;
            }
        }
        return isServiceRunning;
    }
    
    /**
     * 检查网络状态
     * @param context context
     * @return netStatus
     */
    private static int checkNetStatus(Context context)
    {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null || manager.getActiveNetworkInfo() == null)
        {
            Logger.d(TAG,
                    "checkNetStatus -0 ConnectivityManager = null or getActiveNetworkInfo() = null");
            // 返回网络状态
            return FusionMessageType.NetworkMessageType.NET_STATUS_TYPE_DISCONNECTED;
        }
        
        // 获取网络服务对象
        NetworkInfo activeNet = manager.getActiveNetworkInfo();
        
        if (activeNet == null)
        {
            return FusionMessageType.NetworkMessageType.NET_STATUS_TYPE_DISCONNECTED;
        }
        
        // 先获取当前网络状态
        int curState = FusionMessageType.NetworkMessageType.NET_STATUS_TYPE_DISCONNECTED;
        if (activeNet.isAvailable() && activeNet.isConnected())
        {
            String netInfo = activeNet.toString();
            Logger.d(TAG, "network info = " + netInfo);
            
            //有网络，判断网络类型
            int netType = activeNet.getType();
            switch (netType)
            {
                case ConnectivityManager.TYPE_MOBILE:
                    // 是手机卡网络，则判断是2G还是3G
                    Logger.d(TAG,
                            "checkNetStatus -4 ConnectivityManager.TYPE_MOBILE");
                    curState = getMobileNetDetail(activeNet.getSubtype());
                    break;
                case ConnectivityManager.TYPE_WIFI:
                    // 是WIFI方式，直接返回当前的状态
                    Logger.d(TAG,
                            "checkNetStatus -5 ConnectivityManager.TYPE_WIFI");
                    curState = FusionMessageType.NetworkMessageType.NET_STATUS_TYPE_WIFI;
                    break;
                default:
                    break;
            }
        }
        
        // 返回当前的网络状态
        return curState;
        
    }
    
    /**
     * 根据电话网络的速度判断是2G还是3G
     * @param subType 电话网络的网络类型
     * @return 2G还是3G
     */
    private static int getMobileNetDetail(int subType)
    {
        switch (subType)
        {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                // 低速网络，都定义为2G
                // Unknown
                Logger.d(TAG, "getMobileNetDetail -2 NET_TYPE_2G");
                return FusionMessageType.NetworkMessageType.NET_STATUS_TYPE_2G;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                // NOT AVAILABLE YET IN API LEVEL 7
                //                case Connectivity.NETWORK_TYPE_EHRPD:
                //                    return true; // ~ 1-2 Mbps
                //                case Connectivity.NETWORK_TYPE_EVDO_B:
                //                    return true; // ~ 5 Mbps
                //                case Connectivity.NETWORK_TYPE_HSPAP:
                //                    return true; // ~ 10-20 Mbps
                //                case Connectivity.NETWORK_TYPE_IDEN:
                //                    return false; // ~25 kbps 
                //                case Connectivity.NETWORK_TYPE_LTE:
                //                    return true; // ~ 10+ Mbps
                // 高速网络，定义为3G
            default:
                // 未知网络定义为3G网络
                Logger.d(TAG, "getMobileNetDetail -1 NET_TYPE_3G");
                return FusionMessageType.NetworkMessageType.NET_STATUS_TYPE_3G;
        }
    }
}
