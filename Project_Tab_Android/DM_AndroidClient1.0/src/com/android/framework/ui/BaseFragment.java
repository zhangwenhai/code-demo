/*
 * 文件名: BaseFragment.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: Fragment基类
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

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.component.log.Logger;
import com.android.framework.logic.BaseLogicBuilder;
import com.android.framework.logic.ILogic;
import com.android.logic.LogicBuilder;

/**
 * Fragment基类<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public abstract class BaseFragment extends Fragment
{
    
    private static final String TAG = "BaseFragment";
    
    /**
     * 系统的所有logic的缓存创建管理类
     */
    private static BaseLogicBuilder mLogicBuilder = null;
    
    /**
     * 该fragment持有的handler类
     */
    private Handler mHandler = null;
    
    /**
     * 是否独自控制logic监听
     */
    private boolean isPrivateHandler = false;
    
    /**
     * 是否当前处于暂停状态
     */
    private boolean mIsPause;
    
    /**
     * 缓存持有的logic对象的集合
     */
    private final List<ILogic> mLogicList = new ArrayList<ILogic>();
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        if (!isInit())
        {
            setLogicBuilder(LogicBuilder.getInstance(getActivity().getApplicationContext()));
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
        return super.onCreateView(inflater, container, savedInstanceState);
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
                    BaseFragment.this.handleStateMessage(msg);
                }
            };
        }
        return mHandler;
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
    
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
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
    }
    
    /**
     * 设置全局的logic建造管理类<BR>
     * @param logicBuilder
     *      logic建造管理类
     */
    private static final void setLogicBuilder(BaseLogicBuilder logicBuilder)
    {
        BaseFragment.mLogicBuilder = logicBuilder;
    }
    
    /**
     * 加载了mLogicBuilder对象<BR>
     * 判断activiy中是否创建了mLogicBuilder对象
     * @return
     *      是否加载了mLogicBuilder
     */
    protected final boolean isInit()
    {
        return mLogicBuilder != null;
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
        mIsPause = false;
    }
    
    @Override
    public void onPause()
    {
        super.onPause();
        mIsPause = true;
    }
    
    protected boolean isPause()
    {
        return mIsPause;
    }
}
