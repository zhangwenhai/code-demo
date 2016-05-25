/*
 * 文件名: ServiceSender.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 服务发送实现类
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.component.service.app;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.RemoteException;

import com.android.component.log.Logger;
import com.android.common.FusionAction.BaseServiceAction;
import com.android.component.service.app.IAppEntry;
import com.android.component.service.core.IServiceEntry;

/**
 * 服务发送实现类<BR>
 * 负责启动SERVICE并且实现APP调用服务的接口方法
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class ServiceSender implements IServiceSender, IImServiceListener
{
    /**
     * TAG
     */
    private static final String TAG = "ServiceSender";

    /**
     * IServiceSender实例
     */
    private static ServiceSender serviceSender;

    /**
     * Application context对象
     */
    private Context mApplicationContext;

    /**
     * service 进程管理对象
     */
    private IServiceEntry mServiceEntry;

    /**
     * UI界面注册的监听
     */
    private final List<IImServiceListener> mImServiceListeners = new ArrayList<IImServiceListener>();

    private Handler mAppCallbackHandler;

    /**
     * app进程持有的Binder
     */
    private IAppEntry.Stub mBinder = new IAppEntry.Stub()
    {
        @Override
        public void imEventCallback(String msgid, int msgType)
                throws RemoteException
        {
            ServiceSender.this.imEventCallback(msgid, msgType);
        }

        @Override
        public void imRecvConfirmCallback(String msgid) throws RemoteException
        {
            ServiceSender.this.imRecvConfirmCallback(msgid);
        }

        @Override
        public void imMessageCallback(String msgids) throws RemoteException
        {
            if (null != msgids && msgids.length() > 0)
            {
                //                String[] msgIdArray = msgids.split(",");
                //                List<String> msgIds = Arrays.asList(msgIdArray);
                //                List<MessageModel> modelList = MessageDbAdapter.getInstance(mApplicationContext)
                //                        .queryListByIds(msgIds);
                //                ServiceSender.this.imMessageCallback(modelList);
            }
        }

        @Override
        public void syncServerTime(long serverTime) throws RemoteException
        {
            ServiceSender.this.syncServerTime(serverTime);
        }
    };

    /**
     * 进程间连接
     */
    private ServiceConnection mServiceConnection = new ServiceConnection()
    {

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1)
        {
            Logger.i(TAG, "onServiceConnected");
            IServiceEntry serviceEntry = IServiceEntry.Stub.asInterface(arg1);
            try
            {
                serviceEntry.registerCallback(mBinder);
                mServiceEntry = serviceEntry;
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0)
        {

        }

    };

    /**
     * 私有构造方法
     */
    private ServiceSender(Context context)
    {
        mApplicationContext = context;
        HandlerThread appThread = new HandlerThread("appThread");
        appThread.start();
        mAppCallbackHandler = new Handler(appThread.getLooper());
    }

    /**
     * 获取一个IServiceSender实例
     * @param context Context
     *
     * @return IServiceSender
     */
    public static synchronized IServiceSender getIServiceSender(Context context)
    {
        if (null == serviceSender)
        {
            serviceSender = new ServiceSender(context);
        }
        serviceSender.startService();
        return serviceSender;
    }
    
    /**
     * 启动service<BR>
     */
    private void startService()
    {
        Logger.d(TAG, "start service");
        
        Intent intent = new Intent(BaseServiceAction.ACTION);
        mApplicationContext.startService(intent);
        mApplicationContext.bindService(intent,
                mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }
    
    //    @Override
    //    public void imMessageCallback(List<MessageModel> models)
    //    {
    //        //TODO 消息来的时候
    //        IImServiceListener[] listeners = new IImServiceListener[mImServiceListeners.size()];
    //        listeners = mImServiceListeners.toArray(listeners);
    //        for (IImServiceListener serviceListener : listeners)
    //        {
    //            serviceListener.imMessageCallback(models);
    //        }
    //    }
    
    @Override
    public void imEventCallback(String msgid, int msgType)
    {
        //TODO  Event事件处理，正在打字。。正在录音。。
        IImServiceListener[] listeners = new IImServiceListener[mImServiceListeners.size()];
        listeners = mImServiceListeners.toArray(listeners);
        for (IImServiceListener serviceListener : listeners)
        {
            serviceListener.imEventCallback(msgid, msgType);
        }
        
    }
    
    @Override
    public void imRecvConfirmCallback(String msgid)
    {
        //TODO 消息已送达
        IImServiceListener[] listeners = new IImServiceListener[mImServiceListeners.size()];
        listeners = mImServiceListeners.toArray(listeners);
        for (IImServiceListener serviceListener : listeners)
        {
            serviceListener.imEventCallback(msgid, 0);
        }
        
    }
    
    @Override
    public void addImServiceListener(IImServiceListener serviceListener)
    {
        mImServiceListeners.add(serviceListener);
    }
    
    @Override
    public void removeImServiceListener(IImServiceListener serviceListener)
    {
        mImServiceListeners.remove(serviceListener);
    }
    
    @Override
    public void login(String sessionId, String uid)
    {
        try
        {
            mServiceEntry.login(sessionId, uid);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
            mServiceEntry = null;
            startService();
        }
        catch (NullPointerException e)
        {
            Logger.e(TAG, "login entry not ready and setPanding login");
        }
    }
    
    @Override
    public void logout(String uid)
    {
        try
        {
            mServiceEntry.logout(uid);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        catch (NullPointerException e)
        {
            Logger.e(TAG, "logout entry not ready do nothing");
        }
    }
    
    @Override
    public void syncServerTime(long serverTime)
    {
        IImServiceListener[] listeners = new IImServiceListener[mImServiceListeners.size()];
        listeners = mImServiceListeners.toArray(listeners);
        for (IImServiceListener serviceListener : listeners)
        {
            serviceListener.syncServerTime(serverTime);
        }
        
    }
    
    @Override
    public void currentPageSessionId(String sessionid)
    {
        try
        {
            mServiceEntry.currentPageSessionId(sessionid);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
            Logger.e(TAG, "mServiceEntry currentPageSessionId RemoteException ");
        }
    }
    
}
