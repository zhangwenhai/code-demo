package com.android.component.service.app;

interface IAppEntry
{
    void imEventCallback(String msgid, int msgType);
    
    void imRecvConfirmCallback(String msgid);
    
    void imMessageCallback(String msgids);
    
    void syncServerTime(long serverTime);
}