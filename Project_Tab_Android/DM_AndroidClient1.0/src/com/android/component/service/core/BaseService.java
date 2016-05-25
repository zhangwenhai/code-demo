/*
 * 文件名: ServiceSender.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 项目的service，包含IM接收的服务
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.component.service.core;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.android.component.log.Logger;
import com.android.component.service.receiver.ConnectionChangedReceiver;
import com.android.utils.PreferencesUtil;
import com.android.common.FusionAction.BaseServiceAction;
import com.android.common.FusionCode.Common;
import com.android.component.service.app.IAppEntry;
import com.android.component.service.core.IServiceEntry;

/**
 * 项目的service，包含IM接收的服务<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class BaseService extends Service
{
    /**
     * debug tag
     */
    private static final String TAG = "BaseService";
    
    /**
     * 线程队列handler
     */
    private Handler mMessageHandler;
    
    /**
     * App进程的回调
     */
    private IAppEntry mAppEntry;
    
    /**
     * Context
     */
    private Context mContext;
    
    /**
     * 登陆后返回的session
     */
    private String mSessionId;
    
    /**
     * 当前会话页面的会话ID
     */
    private String mCurrentPageSessionId;
    
    /**
     * 用户id
     */
    private String mUid;
    
    /**
     * 注册监听
     */
    private ConnectionChangedReceiver mReceiver = new ConnectionChangedReceiver();
    
    /**
     * 用作界面调用服务层的接口
     */
    private IServiceEntry.Stub mBinder = new IServiceEntry.Stub()
    {
        @Override
        public void registerCallback(IAppEntry appEntry) throws RemoteException
        {
            BaseService.this.mAppEntry = appEntry;
            if (appEntry != null)
            {
                //                appEntry.syncServerTime(RpcUtils.currentTimeMillis());
            }
        }
        
        @Override
        public void login(String sessionId, String uid) throws RemoteException
        {
            BaseService.this.login(sessionId, uid);
        }
        
        @Override
        public void logout(String uid) throws RemoteException
        {
            BaseService.this.logout(uid);
        }
        
        @Override
        public void currentPageSessionId(String sessionid)
                throws RemoteException
        {
            BaseService.this.currentPageSessionId(sessionid);
        }
    };
    
    //    /**
    //     * IM消息推送的回调
    //     */
    //    private IMClientCallback iMClientCallbackListener = new IMClientCallback()
    //    {
    //        
    //        @Override
    //        public void onPipeCreated(String pipeKey)
    //        {
    //            Logger.d(TAG, "IMClientCallback pipeKey: " + pipeKey);
    //        }
    //        
    //        @Override
    //        public String getSessionId()
    //        {
    //            Logger.d(TAG, "IMClientCallback sessionId: " + mSessionId);
    //            return mSessionId;
    //        }
    //        
    //        @Override
    //        public void dealWithMessage(final List<RpcMessageBase> msgList)
    //        {
    //            Logger.e(TAG, "dealWithMessage");
    //            
    //            if (null == msgList || msgList.size() == 0)
    //            {
    //                return;
    //            }
    //            
    //            Runnable runnable = new Runnable()
    //            {
    //                @Override
    //                public void run()
    //                {
    //                }
    //                
    //            };
    //            mMessageHandler.post(runnable);
    //        }
    //        
    //        @Override
    //        public void dealWithKickOut(KickOut msg)
    //        {
    //            // TODO 踢出处理
    //            
    //        }
    //        
    //        @Override
    //        public String getDeviceToken()
    //        {
    //            // 用mac地址或者imei等可以唯一标记客户端的, 也可以随机生成uuid,然后存入配置文件, 后面每次从配置文件读
    //            String deviceId = DeviceInfoUtil.getInstance(mContext)
    //                    .getDeviceID();
    //            Logger.d(TAG, "DeviceID: " + deviceId);
    //            return deviceId;
    //        }
    //    };
    
    /**
     * 注册推送消息的回调<BR>
     * @param sessionId sessionId
     * @param uid uid带@cici
     */
    private void login(String sessionId, String uid)
    {
        this.mSessionId = sessionId;
        this.mUid = uid;
        
        //注册推送消息的回调
        //        IMClientManager.createClient(mContext, mUid, iMClientCallbackListener);
    }
    
    /**
     * 登出是注销消息推送<BR>
     * @param uid uid
     */
    private void logout(String uid)
    {
        Logger.d(TAG, "remote logout");
        //        IMClientManager.destroyClient(uid);
        PreferencesUtil.clearCache();
    }
    
    /**
     * 登出是注销消息推送<BR>
     * @param notifyid notifyid
     */
    private void currentPageSessionId(String sessionid)
    {
        mCurrentPageSessionId = sessionid;
    }
    
    /**
     * 初始化资源数据时候得到当前的sessionid 和userid<BR>
     */
    private void initResoure()
    {
        mSessionId = PreferencesUtil.getAttrString(Common.KEY_SESSION_ID);
        mUid = PreferencesUtil.getAttrString(Common.KEY_USER_ID);
        
        Logger.i(TAG, "onCreate initResoure sessionId is " + mSessionId);
        Logger.i(TAG, "onCreate initResoure uid is " + mUid);
        
    }
    
    /**
     * 给服务器做回执用的<BR>
     */
    private void initDelegateCenter(Context context)
    {
        final String dataDir = context.getFilesDir().getPath();
        //        RpcDelegateCenter.singleton().setDelegate(new RpcDelegate()
        //        {
        //            @Override
        //            public String getSesionKey()
        //            {
        //                Logger.d(TAG, "RpcDelegateCenter mSessionId is " + mSessionId);
        //                return mSessionId;
        //            }
        //            
        //            @Override
        //            public int getClientVersion()
        //            {
        //                return 0;
        //            }
        //            
        //            @Override
        //            public String getUid()
        //            {
        //                
        //                if (!TextUtils.isEmpty(mSessionId))
        //                {
        //                    return mUid;
        //                }
        //                return null;
        //            }
        //            
        //            @Override
        //            public String getAcceptLanguage()
        //            {
        //                return null;
        //            }
        //            
        //            @Override
        //            public void onRpcKickOut()
        //            {
        //            }
        //            
        //            @Override
        //            public String getUUID()
        //            {
        //                return "1234";
        //            }
        //            
        //            @Override
        //            public String getVersionStr()
        //            {
        //                return "1.0.0.1";
        //            }
        //            
        //            @Override
        //            public String getDeviceModel()
        //            {
        //                return "Android";
        //            }
        //            
        //            @Override
        //            public String getDeviceVersion()
        //            {
        //                return "2.3.1";
        //            }
        //            
        //            @Override
        //            public int getScreenWidth()
        //            {
        //                return 320;
        //            }
        //            
        //            @Override
        //            public int getScreenHeight()
        //            {
        //                return 480;
        //            }
        //            
        //            @Override
        //            public int getScreenScale()
        //            {
        //                return RpcConstants.SCREEN_SCALE_MULTIP;
        //            }
        //            
        //            @Override
        //            public int getDeviceType()
        //            {
        //                return DeviceType.ANDROID_TYPE;
        //            }
        //            
        //            @Override
        //            public String getDataDir()
        //            {
        //                return dataDir;
        //            }
        //        });
    }
    
    @Override
    public void onCreate()
    {
        Logger.i(TAG, "onCreate start");
        
        super.onCreate();
        //注册时间 兼听
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(mReceiver, filter);
        
        //        Crashlytics.start(this);
        mContext = getApplicationContext();
        
        initSystem();
        
        HandlerThread thread = new HandlerThread("MessageThread");
        thread.start();
        mMessageHandler = new Handler(thread.getLooper());
        
        //        RpcUtils.registerRpcUtilsListener(new RpcUtilsListener()
        //        {
        //            
        //            @Override
        //            public void onTimeSynced(long serverTime)
        //            {
        //                try
        //                {
        //                    if (null != mAppEntry)
        //                    {
        //                        mAppEntry.syncServerTime(serverTime);
        //                    }
        //                }
        //                catch (RemoteException e)
        //                {
        //                    e.printStackTrace();
        //                }
        //            }
        //        });
        
        Logger.d(TAG, "onCreate sessionId get from sharedpreferences is "
                + mSessionId);
        if (!TextUtils.isEmpty(mSessionId))
        {
            Logger.d(TAG, "login im");
            login(mSessionId, mUid);
        }
        Logger.i(TAG, "onCreate finished!");
    };
    
    /**
     * 系统初始化<BR>
     */
    private void initSystem()
    {
        Logger.configureLogback(mContext);
        //        RpcUtils.setDebug(FusionConfig.getInstance().isDebug());
        //最早初始化
        PreferencesUtil.initContext(mContext);
        //取出是否有登陆的sessionid
        initResoure();
        //给服务器做回执用的 
        initDelegateCenter(mContext);
        //在本次进程中重新初始化服务器地址
        initServerAddr();
    }
    
    /**
     * 初始化服务器地址<BR>
     */
    private void initServerAddr()
    {
        //        if (!BuildConfig.IS_DEBUG)
        //        {
        //            //            GetServerInfo info = new GetServerInfo()
        //            //            {
        //            //                
        //            //                @Override
        //            //                public void onResponse()
        //            //                {
        //            //                    super.onResponse();
        //            //                    if (isOK())
        //            //                    {
        //            //                        ServerConfig.setUrls(this.getResult().getUrls());
        //            //                    }
        //            //                }
        //            //                
        //            //                @Override
        //            //                public void onLoadedCache()
        //            //                {
        //            //                    
        //            //                    super.onLoadedCache();
        //            //                    ServerConfig.setUrls(this.getResult().getUrls());
        //            //                }
        //            //            };
        //            //            info.call("1.0.0");
        //            return;
        //        }
        //        String url = "192.168.6.12:8081";
        //        //String url = "192.168.1.26:8080";
        //        ServerConfig.setDefaultUrls(new String[] { "http://" + url
        //                + "/kiss-product/" });
        //        ServerConfig.setUrls(ServerConfig.CATEGORY_RESOURCE,
        //                new String[] { "http://" + url + "/service-resource/" });
        //        ServerConfig.setUrls(ServerConfig.CATEGORY_AUTH,
        //                new String[] { "http://" + url + "/service-auth/" });
        //        ServerConfig.setUrls(ServerConfig.CATEGORY_IM, new String[] { "http://"
        //                + url + "/kiss-business/" });
        //        ServerConfig.setUrls(ServerConfig.CATEGORY_LONG,
        //                new String[] { "ws://192.168.6.12:9090/websocket/" });
        //        ServerConfig.setUrls(ServerConfig.CATEGORY_BUSINESS,
        //                new String[] { "http://" + url + "/kiss-business/" });
        //        
        //        ServerConfig.setUrls(ServerConfig.CATEGORY_HTTPIMAGE,
        //                new String[] { "http://" + url + "/service-resource/image" });
        //        ServerConfig.setUrls(ServerConfig.CATEGORY_HTTPVOICE,
        //                new String[] { "http://" + url + "/service-resource/voice" });
        //        ServerConfig.setUrls(ServerConfig.CATEGORY_LOG,
        //                new String[] { "http://" + url + "/service-resource/" });
    }
    
    @Override
    public IBinder onBind(Intent arg0)
    {
        Logger.i(TAG, "onBind");
        return mBinder;
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Logger.i(TAG, "onStartCommand");
        // 保证服务重启
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }
    
    @Override
    public void onDestroy()
    {
        unregisterReceiver(mReceiver);
        // android.os.Process.killProcess(android.os.Process.myPid());
        // 销毁时重新启动Service
        Intent intent = new Intent(BaseServiceAction.ACTION);
        this.startService(intent);
        super.onDestroy();
    }
    
}
