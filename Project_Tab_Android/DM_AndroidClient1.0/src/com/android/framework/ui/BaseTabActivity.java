/*
 * 文件名: BaseTabActivity.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: TabActivity基类
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.framework.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.android.component.log.Logger;
import com.android.framework.logic.BaseLogicBuilder;
import com.android.framework.logic.ILogic;

/**
 * TabActivity基类<BR>
 * 
 * @author zhangwenhai
 * @version [Paitao Client V20130911, 2013-9-12] 
 */
@SuppressLint("HandlerLeak")
@SuppressWarnings("deprecation")
public abstract class BaseTabActivity extends TabActivity
{
    
    private static final String TAG = "BaseTabActivity";
    
    /**
     * 系统的所有logic的缓存创建管理类
     */
    private static BaseLogicBuilder mLogicBuilder = null;
    
    /**
     * 该activity持有的handler类
     */
    private Handler mHandler = null;
    
    /**
     * 是否独自控制logic监听
     */
    private boolean isPrivateHandler = false;
    
    /**
     * 缓存持有的logic对象的集合
     */
    private final List<ILogic> mLogicList = new ArrayList<ILogic>();
    
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
            mLogicBuilder.addHandlerToAllLogics(getHandler());
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
    
    /**
     * 获取hander对象<BR>
     * @return 返回handler对象
     */
    protected Handler getHandler()
    {
        if (mHandler == null)
        {
            mHandler = new Handler()
            {
                public void handleMessage(Message msg)
                {
                    BaseTabActivity.this.handleStateMessage(msg);
                }
            };
        }
        return mHandler;
    }
    
    /**
     * activity是否已经初始化，加载了mLogicBuilder对象<BR>
     * 判断activiy中是否创建了mLogicBuilder对象
     * @return
     *      是否加载了mLogicBuilder
     */
    protected final boolean isInit()
    {
        return mLogicBuilder != null;
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
        if (isPrivateHandler())
        {
            logic.addHandler(getHandler());
            mLogicList.add(logic);
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
     * logic通过handler回调的方法<BR>
     * 通过子类重载可以实现各个logic的sendMessage到handler里的回调方法
     * @param msg
     *      Message对象
     */
    protected void handleStateMessage(Message msg)
    {
        
    }
    
    /**
     * activity的释放的方法<BR>
     * 在这里对所有加载到logic中的handler进行释放
     * @see android.app.ActivityGroup#onDestroy()
     */
    protected void onDestroy()
    {
        Handler handler = getHandler();
        if (handler != null)
        {
            if (mLogicList.size() > 0 && isPrivateHandler())
            {
                for (ILogic logic : mLogicList)
                {
                    logic.removeHandler(handler);
                }
            }
            else if (mLogicBuilder != null)
            {
                mLogicBuilder.removeHandlerToAllLogics(handler);
            }
            
        }
        super.onDestroy();
    }
    
    /**
     * 设置全局的logic建造管理类<BR>
     * @param logicBuilder
     *      logic建造管理类
     */
    protected static final void setLogicBuilder(BaseLogicBuilder logicBuilder)
    {
        BaseTabActivity.mLogicBuilder = logicBuilder;
    }
}
