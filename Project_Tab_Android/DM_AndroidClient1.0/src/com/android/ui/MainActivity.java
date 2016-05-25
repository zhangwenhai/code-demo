/*
 * 文件名: MainActivity.java
 * 版    权： Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 系统的初始化类
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.ui;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.android.common.FusionMessageType;
import com.android.ui.basic.BasicActivity;
import com.android.utils.StringUtil;
import com.android.R;
import com.android.common.FusionAction.MainTabAction;

/**
 * 系统的初始化类<BR>
 *
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class MainActivity extends BasicActivity
{
    /**
     * TAG
     */
    public static final String TAG = "MainActivity";

    /**
     * 2秒常量
     */
    private static final int TWO_SECOND = 500;

    private static boolean mToLogin;

    /**
     * 启动
     *
     * @param savedInstanceState
     *            Bundle
     * @see com.paitao.basic.android.ui.basic.BasicActivity#onCreate(Bundle)
     */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //        Crashlytics.start(this);
        setContentView(R.layout.main);

        if (mToLogin)
        {
            Intent intent = new Intent(MainTabAction.ACTION);
            startActivity(intent);
            finish();
            mToLogin = false;
            return;
        }
        doLogin();
    }

    /**
     * 返回一个boolean表示展示该页面是否需要登录成功
     *
     * @return boolean 是否是登录后的页面
     */
    @Override
    protected boolean needLogin()
    {
        // 此页面不需要先登录
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void handleStateMessage(Message msg)
    {
        super.handleStateMessage(msg);
        // 如果界面销毁了 就不处理
        if (isFinishing())
        {
            return;
        }

        switch (msg.what)
        {
            case FusionMessageType.MainMessageType.MSGTYPE_TIMER_LOGIN:
                // 检测当前android的界面是不是我们的应用
                if (StringUtil.equals(((ActivityManager) getSystemService(
                                ACTIVITY_SERVICE)).getRunningTasks(1)
                                .get(0).baseActivity.getPackageName(),
                        getPackageName()))
                {
                    // 跳转登陆界面
                    Intent intent = new Intent(MainTabAction.ACTION);
                    startActivity(intent);
                }
                else
                {
                    mToLogin = true;
                }
                finish();
                break;

            case FusionMessageType.MainMessageType.MSGTYPE_TIMER_MAINTAB:

                break;
            default:
                break;
        }
    }

    /**
     * 执行登陆操作<BR>
     */
    private void doLogin()
    {
        // TODO判断状态是否已经登陆
        sendEmptyMessageDelayed(FusionMessageType.MainMessageType.MSGTYPE_TIMER_LOGIN,
                TWO_SECOND);
    }
}
