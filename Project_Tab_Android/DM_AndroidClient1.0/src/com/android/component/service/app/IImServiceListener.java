/*
 * 文件名: IImServiceListener.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: im消息的回调
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.component.service.app;


/**
 * im消息的回调<BR>
 * @author zhangwenhai
 * @version [Paitao Client V20130911, 2013-11-19] 
 */
public interface IImServiceListener
{
    /**
     * IMservice层监听器 Event事件处理，正在打字。。正在录音。。。<BR>
     * @param msgid 消息id
     * @param msgType 消息类型
     */
    void imEventCallback(String msgid, int msgType);
    
    /**
     * IMservice层监听器 消息已送达<BR>
     * @param msgid 消息id
     */
    void imRecvConfirmCallback(String msgid);
    
    /**
     * 同步服务器时间<BR>
     * @param serverTime 服务器时间
     */
    void syncServerTime(long serverTime);
    
}
