/*
 * 文件名: BaseActivity.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: Activity的抽象基类
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.framework.ui;

import java.util.HashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

import com.android.component.log.Logger;
import com.android.framework.logic.BaseLogicBuilder;
import com.android.framework.logic.ILogic;
import com.android.framework.logic.ILogicBuilder;
import com.android.utils.DisplayInfo;

/**
 * Activity的抽象基类<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
@SuppressLint("HandlerLeak")
public abstract class BaseActivity extends FragmentActivity
{
    /**
     * TAG
     */
    private static final String TAG = "BaseActivity";
    
    /**
     * 系统的所有logic的缓存创建管理类
     */
    private static BaseLogicBuilder mLogicBuilder = null;
    
    /**
     * 该activity持有的handler类
     */
    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if (mHandler != null)
            {
                BaseActivity.this.handleStateMessage(msg);
            }
        }
    };
    
    /**
     * 是否独自控制logic监听
     */
    private boolean isPrivateHandler = false;
    
    /**
     * 缓存持有的logic对象的集合
     */
    private final Set<ILogic> mLogicSet = new HashSet<ILogic>();
    
    /**
     * Acitivity的初始化方法<BR>
     * @param savedInstanceState
     *     Bundle对象
     * @see android.app.ActivityGroup#onCreate(Bundle)
     */
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (!isInit())
        {
            Logger.e(TAG,
                    "Launched the first should be the LauncheActivity's subclass:"
                            + this.getClass().getName(),
                    new Throwable());
            return;
        }
        
        if (!isPrivateHandler())
        {
            BaseActivity.mLogicBuilder.addHandlerToAllLogics(getHandler());
        }
        try
        {
            initLogics();
        }
        catch (Exception e)
        {
            Logger.e(TAG, "Init logics failed :" + e.getMessage(), e);
        }
        //设置在当前应用界面，调用音量键的时候控制的是多媒体音量
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }
    
    @Override
    protected void onPause()
    {
        super.onPause();
        // 因为window的尺寸再显示之后才能确定，onCreate 与 onResume 时都无法取得，
        // 所以选择在启动页的oPause时初始化，便于其他页面使用
        DisplayInfo.init(this);
    }
    
    /**
     * 获取hander对象<BR>
     * @return 返回handler对象
     */
    protected final Handler getHandler()
    {
        return mHandler;
    }
    
    /**
     * 发送消息
     * @param what
     *      消息标识
     */
    protected final void sendEmptyMessage(int what)
    {
        if (mHandler != null)
        {
            mHandler.sendEmptyMessage(what);
        }
    }
    
    /**
     * 延迟发送空消息
     * @param what
     *      消息标识
     * @param delayMillis
     *      延迟时间
     */
    protected final void sendEmptyMessageDelayed(int what, long delayMillis)
    {
        if (mHandler != null)
        {
            mHandler.sendEmptyMessageDelayed(what, delayMillis);
        }
    }
    
    /**
     * 
     * post一段操作到UI线程
     * @param runnable Runnable
     */
    protected final void postRunnable(Runnable runnable)
    {
        if (mHandler != null)
        {
            mHandler.post(runnable);
        }
    }
    
    /**
     * 发送消息
     * @param msg
     *      消息对象
     */
    protected final void sendMessage(Message msg)
    {
        if (mHandler != null)
        {
            mHandler.sendMessage(msg);
        }
    }
    
    /**
     * 延迟发送消息
     * @param msg
     *      消息对象
     * @param delayMillis
     *      延迟时间
     */
    protected final void sendMessageDelayed(Message msg, long delayMillis)
    {
        if (mHandler != null)
        {
            mHandler.sendMessageDelayed(msg, delayMillis);
        }
    }
    
    /**
     * activity是否已经初始化，加载了mLogicBuilder对象<BR>
     * 判断activiy中是否创建了mLogicBuilder对象
     * @return
     *      是否加载了mLogicBuilder
     */
    protected final boolean isInit()
    {
        return BaseActivity.mLogicBuilder != null;
    }
    
    /**
     * 获取全局的LogicBuilder对象<BR>
     * @return
     *      返回LogicBuilder对象
     */
    public static ILogicBuilder getLogicBuilder()
    {
        return BaseActivity.mLogicBuilder;
    }
    
    /**
     * 判断UI是否独自管理对logic的handler监听<BR>
     * @return
     *      是否是私有监听的handler
     */
    protected boolean isPrivateHandler()
    {
        return isPrivateHandler;
    }
    
    /**
     * 初始化logic的方法，由子类实现<BR>
     * 在该方法里通过getLogicByInterfaceClass获取logic对象
     */
    protected abstract void initLogics();
    
    /**
     * 通过接口类获取logic对象<BR>
     * @param interfaceClass
     *      接口类型
     * @return
     *      logic对象
     */
    protected final ILogic getLogicByInterfaceClass(Class<?> interfaceClass)
    {
        ILogic logic = mLogicBuilder.getLogicByInterfaceClass(interfaceClass);
        if (isPrivateHandler() && null != logic && !mLogicSet.contains(logic))
        {
            logic.addHandler(getHandler());
            mLogicSet.add(logic);
        }
        if (logic == null)
        {
            Logger.e(TAG, "Not found logic by interface class ("
                    + interfaceClass + ")", new Throwable());
            return null;
        }
        return logic;
    }
    
    /**
     * 设置全局的logic建造管理类<BR>
     * @param logicBuilder
     *      logic建造管理类
     */
    protected static final void setLogicBuilder(BaseLogicBuilder logicBuilder)
    {
        BaseActivity.mLogicBuilder = logicBuilder;
    }
    
    /**
     * logic通过handler回调的方法<BR>
     * 通过子类重载可以实现各个logic的sendMessage到handler里的回调方法
     * @param msg
     *      Message对象
     */
    protected void handleStateMessage(Message msg)
    {
        
    }
    
    /**
     * 结束Activity
     * @see android.app.Activity#finish()
     */
    public void finish()
    {
        removeHandler();
        super.finish();
    }
    
    /**
     * activity的释放的方法<BR>
     * 在这里对所有加载到logic中的handler进行释放
     * @see android.app.ActivityGroup#onDestroy()
     */
    protected void onDestroy()
    {
        removeHandler();
        super.onDestroy();
    }
    
    private void removeHandler()
    {
        if (this.mHandler != null)
        {
            if (mLogicSet.size() > 0 && isPrivateHandler())
            {
                for (ILogic logic : mLogicSet)
                {
                    logic.removeHandler(this.mHandler);
                }
            }
            else if (mLogicBuilder != null)
            {
                mLogicBuilder.removeHandlerToAllLogics(this.mHandler);
            }
            this.mHandler = null;
        }
    }
}
