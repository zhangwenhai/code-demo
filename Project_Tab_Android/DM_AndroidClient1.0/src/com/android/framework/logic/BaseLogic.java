/*
 * 文件名: BaseLogic.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: logic抽象类，所有的业务实现logic必须继承
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.framework.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

/**
 * logic抽象类，所有的业务实现logic必须继承<BR>
 * 提供handler和监听数据库等其他接口实现
 * @author zhangwenhai
 * @version [Paitao Client V20130911, 2013-9-12]
 */
public class BaseLogic implements ILogic
{
    
    private static final String TAG = "BaseLogic";
    
    /**
     * logic对象中UI监听的handler缓存集合
     */
    private final List<Handler> mHandlerList = new Vector<Handler>();
    
    /**
     * 数据库Uri对应的Observer对象的集合
     */
    private final Map<Uri, ContentObserver> mObserverCache = new HashMap<Uri, ContentObserver>();
    
    /**
     * 系统的context对象
     */
    private Context mContext;
    
    private Handler mHandler;
    
    /**
     * 初始化方法<BR>
     * 在被系统管理的logic在注册到LogicBuilder中后立即被调用的初始化方法。
     * 
     * @param context
     *            系统的context对象
     */
    @SuppressLint("HandlerLeak")
    public void init(Context context)
    {
        this.mContext = context;
        mHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                synchronized (mHandlerList)
                {
                    // 在logic中需要延迟几秒处理的
                    if (msg.obj == null)
                    {
                        onSendEmptyMessageDelayed(msg.what);
                    }
                    else
                    {
                        onSendMessageDelayed(msg);
                    }
                    
                    // 通过监听回调，延迟通知在该logic对象中所有注册了handler的UI消息message对象
                    for (Handler handler : mHandlerList)
                    {
                        if (!handler.hasMessages(msg.what))
                        {
                            if (msg.obj == null)
                            {
                                handler.sendEmptyMessage(msg.what);
                            }
                            else
                            {
                                Message message = new Message();
                                message.what = msg.what;
                                message.obj = msg.obj;
                                handler.sendMessage(message);
                            }
                        }
                    }
                }
            }
        };
    }
    
    /**
     * 对logic增加handler<BR>
     * 在logic对象里加入UI的handler
     * 
     * @param handler
     *            UI传入的handler对象
     */
    public final void addHandler(Handler handler)
    {
        //        Logger.d(TAG, "In add hander method." + this.getClass().getName()
        //                + " have " + mHandlerList.size() + " hander.");
        mHandlerList.add(handler);
    }
    
    /**
     * 对logic移除handler<BR>
     * 在logic对象里移除UI的handler
     * 
     * @param handler
     *            UI传入的handler对象
     */
    public final void removeHandler(Handler handler)
    {
        //        Logger.d(TAG, "In remove hander method." + this.getClass().getName()
        //                + " have " + mHandlerList.size() + " hander.");
        mHandlerList.remove(handler);
    }
    
    /**
     * 对URI注册鉴定数据库表<BR>
     * 根据URI监听数据库表的变化，放入监听对象缓存
     * 
     * @param uri
     *            数据库的Content Provider的 Uri
     */
    public final void registerObserver(final Uri uri)
    {
        ContentObserver observer = new ContentObserver(new Handler())
        {
            public void onChange(final boolean selfChange)
            {
                BaseLogic.this.onChangeByUri(selfChange, uri);
            }
        };
        mContext.getContentResolver().registerContentObserver(uri,
                true,
                observer);
        mObserverCache.put(uri, observer);
    }
    
    /**
     * 对URI解除注册鉴定数据库表<BR>
     * 根据URI移除对数据库表变化的监听，移出监听对象缓存
     * 
     * @param uri
     *            数据库的Content Provider的 Uri
     */
    public final void unRegisterObserver(Uri uri)
    {
        ContentObserver observer = mObserverCache.get(uri);
        if (observer != null)
        {
            mContext.getContentResolver().unregisterContentObserver(observer);
            mObserverCache.remove(uri);
        }
    }
    
    /**
     * 当对数据库表定义的Uri进行监听后，被回调方法<BR>
     * 子类中需重载该方法，可以在该方法的代码中对表变化进行监听实现
     * 
     * @param selfChange
     *            如果是true，被监听是由于代码执行了commit造成的
     * @param uri
     *            被监听的Uri
     */
    public void onChangeByUri(boolean selfChange, Uri uri)
    {
        
    }
    
    /**
     * 发送消息给UI<BR>
     * 通过监听回调，通知在该logic对象中所有注册了handler的UI消息message对象
     * 
     * @param what
     *            返回的消息标识
     * @param obj
     *            返回的消息数据对象
     */
    public void sendMessage(int what, Object obj)
    {
        synchronized (mHandlerList)
        {
            for (Handler handler : mHandlerList)
            {
                if (obj == null)
                {
                    handler.sendEmptyMessage(what);
                }
                else
                {
                    Message message = handler.obtainMessage(what, obj);
                    handler.sendMessage(message);
                }
            }
        }
    }
    
    /**
     * 发送无数据对象消息给UI<BR>
     * 通过监听回调，通知在该logic对象中所有注册了handler的UI消息message对象
     * 
     * @param what
     *            返回的消息标识
     */
    public void sendEmptyMessage(int what)
    {
        synchronized (mHandlerList)
        {
            for (Handler handler : mHandlerList)
            {
                handler.sendEmptyMessage(what);
            }
        }
    }
    
    /**
     * 延迟发送空消息给UI<BR>
     * 通过监听回调，延迟通知在该logic对象中所有注册了handler的UI消息message对象
     * 
     * @param what
     *            返回的消息标识
     * @param delayMillis
     *            延迟时间，单位秒
     */
    public void sendEmptyMessageDelayed(int what, long delayMillis)
    {
        if (!mHandler.hasMessages(what))
        {
            mHandler.sendEmptyMessageDelayed(what, delayMillis);
        }
        
    }
    
    /**
     * 延迟发送消息给UI<BR>
     * 通过监听回调，延迟通知在该logic对象中所有注册了handler的UI消息message对象
     * 
     * @param what
     *            返回的消息标识
     * @param obj
     *            返回的消息数据对象
     * @param delayMillis
     *            延迟时间，单位秒
     */
    public void sendMessageDelayed(int what, Object obj, long delayMillis)
    {
        if (!mHandler.hasMessages(what))
        {
            Message msg = new Message();
            msg.what = what;
            msg.obj = obj;
            mHandler.sendMessageDelayed(msg, delayMillis);
        }
    }
    
    /**
     * 移除消息<BR>
     * @param what 消息key
     */
    public void removeMessages(int what)
    {
        mHandler.removeMessages(what);
    }
    
    /**
     * 延迟发送消息给logic<BR>
     * 通过监听回调，延迟通知在该logic对象中 {@link sendEmptyMessageDelayed}
     * 
     * @param what
     *            返回的消息标识
     */
    protected void onSendEmptyMessageDelayed(int what)
    {
    }
    
    /**
     * 延迟发送消息给logic<BR>
     * 通过监听回调，延迟通知在该logic对象中 {@link sendMessageDelayed}
     * 
     * @param msg
     *            返回的消息标识 和 消息数据对象
     */
    protected void onSendMessageDelayed(Message msg)
    {
    }
}
