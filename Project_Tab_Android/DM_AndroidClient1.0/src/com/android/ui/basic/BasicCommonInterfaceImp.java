/*
 * 文件名: BasicCommonInterfaceImp.java
 * 版    权： Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 基类的公共方法接口实现类
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.ui.basic;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;

import com.android.ui.basic.dialog.EditDialog;
import com.android.R;
import com.android.common.FusionCode.Common;

/**
 * 基类公共接口实现类<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class BasicCommonInterfaceImp implements com.android.ui.basic.BasicCommonInterface
{

    private Activity mContext;

    /**
     * 构造方法
     * @param activity 上下文
     */
    public BasicCommonInterfaceImp(Activity activity)
    {
        mContext = activity;
    }

    @Override
    public boolean hasLogined()
    {
        //TODO
        return true;
        //        return !TextUtils.isEmpty(FusionConfig.getInstance().getSessionId());
    }

    @Override
    public void slideInFromBottom()
    {
        mContext.overridePendingTransition(R.anim.slide_in_from_bottom,
                R.anim.vertical_static);
    }

    @Override
    public void slideOutToBottom()
    {
        mContext.overridePendingTransition(R.anim.vertical_static,
                R.anim.slide_out_to_bottom);
    }

    @Override
    public void slideInFromRight()
    {
        mContext.overridePendingTransition(R.anim.slide_in_from_right,
                R.anim.slide_out_to_left);
    }

    @Override
    public void slideOutFromLeft()
    {
        mContext.overridePendingTransition(R.anim.slide_in_from_left,
                R.anim.slide_out_to_right);
    }

    @Override
    public SharedPreferences getSharedPreferences()
    {
        return mContext.getSharedPreferences(Common.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
    }

    @Override
    public EditDialog showEditDialog(String title, String left, String right,
            final OnClickListener okListener)
    {
        return new EditDialog(mContext).setNegativeButton(left, null)
                .setPositiveButton(right, new OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        okListener.onClick(dialog, which);
                    }
                })
                .setTitle(title);
    }
}
