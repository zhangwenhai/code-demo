/*
 * 文件名: IServiceSender.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: app与service交互的发送接口定义
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.component.service.app;

/**
 * app与service交互的发送接口定义<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public interface IServiceSender
{
    /**
     * 登陆以后注册推送消息的回调<BR>
     * @param sessionId sessionId
     * @param uid uid带@cici
     */
    public void login(String sessionId, String uid);
    
    /**
    * 登出是注销消息推送<BR>
    * @param uid uid带@cici
    */
    public void logout(String uid);
    
    /**
     * 添加监听<BR>
     * @param serviceListener IImServiceListener
     */
    void addImServiceListener(IImServiceListener serviceListener);
    
    /**
     * 移除监听<BR>
     * @param serviceListener IImServiceListener
     */
    void removeImServiceListener(IImServiceListener serviceListener);
    
    /**
     * 当前页面的sessionid<BR>
     * @param sessionid sessionid
     */
    void currentPageSessionId(String sessionid);
}
