/*
 * 文件名: BootReceiver.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 监听系统开关机
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.component.service.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.component.log.Logger;
import com.android.common.FusionAction.BaseServiceAction;

/**
 * 监听系统开关机<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class BootReceiver extends BroadcastReceiver
{
    /**
     * TAG
     */
    public static final String TAG = "BootReceiver";
    
    /**
     * 构造
     * @param context 上下文
     * @param intent intent
     * @see BroadcastReceiver#onReceive(Context, Intent)
     */
    @Override
    public void onReceive(final Context context, Intent intent)
    {
        //系统开机
        if (null != intent
                && Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
        {
            Logger.i(TAG, "sendBootMessage!");
            //TODO 这里可以传入网络状态
            Intent intent1 = new Intent(BaseServiceAction.ACTION);
            context.startService(intent1);
        }
    }
}
