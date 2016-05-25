package com.android.component.service.core;
import com.android.component.service.app.IAppEntry;


interface IServiceEntry
{
    // 注册回调对象
    void registerCallback(IAppEntry appEntry);

    // 登录接口
    void login(String sessionId,String uid);

    //登出接口
    void logout(String uid);
    
    //当前的会话界面
    void currentPageSessionId(String sessionid);
}