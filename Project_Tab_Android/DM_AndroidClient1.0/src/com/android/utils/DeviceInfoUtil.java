/*
 * 文件名: DeviceInfoUtil.java
 * 版    权： Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 获取设备信息
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.provider.Settings.Secure;

import com.android.component.log.Logger;

/**
 * 获取设备信息<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-9]
 */
public class DeviceInfoUtil
{
    
    /**
     * 单例对象
     */
    private static DeviceInfoUtil mInstance;
    
    /**
     * deviceID
     */
    private String mDeviceID;
    
    /**
     * Context
     */
    private Context mContext;
    
    /**
     * 构造器
     * 
     * @param context
     *            Context
     */
    public DeviceInfoUtil(Context context)
    {
        mContext = context;
        mDeviceID = getAndroidId();
        // TODO 目前只需要 device id 以后关于设备信息的往这个类里加。
    }
    
    /**
     * 单例 对象
     * 
     * @param context
     *            Context
     * @return DeviceInfoUtil
     */
    public static synchronized DeviceInfoUtil getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new DeviceInfoUtil(context);
        }
        return mInstance;
    }
    
    /**
     * 获得android设备唯一标识<BR>
     * 
     * @return deviceID
     */
    private String getAndroidId()
    {
        return Secure.getString(mContext.getContentResolver(),
                Secure.ANDROID_ID);
    }
    
    /**
     * 获得android设备唯一标识，android2.2 之前无法稳定运行.
     * 
     * @return deviceID
     */
    public String getDeviceID()
    {
        return mDeviceID;
    }
    
    /**
     * Get IP address from first non-localhost interface
     * 
     * @return address or empty string
     */
    public static String getIPAddress()
    {
        try
        {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces)
            {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs)
                {
                    if (!addr.isLoopbackAddress())
                    {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        return sAddr;
                    }
                }
            }
        }
        catch (Exception e)
        {
            Logger.d("DeviceInfoUtil", "Error: " + e.toString());
        }
        return "";
    }
    
}
