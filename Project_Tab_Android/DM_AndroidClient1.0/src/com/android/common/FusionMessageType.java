/*
 * 文件名: FusionMessageType.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 消息类型定义
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.common;

/**
 * 消息类型定义<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public interface FusionMessageType
{
    /**
     * Main初始化页面相关消息码 <BR>
     * @author zhangwenhai
     * @version [Client V20150307, 2015-3-8]
     */
    public interface MainMessageType
    {
        /**
         * 此MainMessageType的基数
         */
        int BASE = 0x10000000;
        
        /**
         * 跳转登陆界面的消息
         */
        int MSGTYPE_TIMER_LOGIN = BASE + 1;
        
        /**
         * 跳转主界面的消息
         */
        int MSGTYPE_TIMER_MAINTAB = BASE + 2;
    }
    
    /**
     * 网络通知消息类型<BR>
     * @author zhangwenhai
     * @version [Client V20150307, 2015-3-8]
     */
    public interface NetworkMessageType
    {
        /**
         * 此MessageType的基数
         */
        int BASE = 0x11000000;
        
        /**
         * 网络变化通知消息
         */
        int NET_STATUS_CHANGED_MSG = BASE + 1;
        
        /**
         * 网络状态：连接中
         */
        int NET_STATUS_CONNECTING = BASE + 2;
        
        /**
         * 网络类型：2G
         */
        int NET_STATUS_TYPE_2G = BASE + 3;
        
        /**
         * 网络类型：3G
         */
        int NET_STATUS_TYPE_3G = BASE + 4;
        
        /**
         * 网络类型：WIFI
         */
        int NET_STATUS_TYPE_WIFI = BASE + 5;
        
        /**
         * 网络状态：网络断开
         */
        int NET_STATUS_TYPE_DISCONNECTED = BASE + 6;
        
        /**
         * 未初始化的网络状态
         */
        int NET_STATUS_TYPE_UNINIT = BASE + 7;
    }
}
