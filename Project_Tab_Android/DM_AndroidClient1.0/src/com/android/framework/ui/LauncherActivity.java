/*
 * 文件名: LauncherActivity.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 系统的Activity的启动类
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.framework.ui;

import android.content.Context;
import android.os.Bundle;

import com.android.component.log.Logger;
import com.android.framework.logic.BaseLogicBuilder;

/**
 * 系统的Activity的启动类<BR>
 * 第一个启动的Activity必须继承，而且其他Activity不要继承
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public abstract class LauncherActivity extends BaseActivity
{
    /**
     * TAG
     */
    private static final String TAG = "LauncheActivity";

    /**
     * Activity的初始化方法<BR>
     * @param savedInstanceState
     *     传入的Bundle对象
     * @see com.paitao.basic.android.framework.ui.BaseActivity#onCreate(Bundle)
     */
    protected void onCreate(Bundle savedInstanceState)
    {
        if (!isInit())
        {
            BaseLogicBuilder logicBuilder = createLogicBuilder(this.getApplicationContext());
            super.setLogicBuilder(logicBuilder);
            initSystem(getApplicationContext());
            
            Logger.i(TAG, "Load logic builder successful");
        }
        super.onCreate(savedInstanceState);
    }
    
    /**
     * 系统的初始化方法<BR>
     * @param context
     *      系统的context对象
     */
    protected abstract void initSystem(Context context);
    
    /**
     * Logic建造管理类需要创建的接口<BR>
     * 需要子类继承后，指定Logic建造管理类具体实例
     * @param context
     *      系统的context对象
     * @return
     *      Logic建造管理类具体实例
     */
    protected abstract BaseLogicBuilder createLogicBuilder(Context context);
}
