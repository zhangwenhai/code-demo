/*
 * 文件名: BasicFragment.java
 * 版    权： Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 提供基本方法的fragment基类
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.ui.basic;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.framework.ui.BaseFragment;
import com.android.ui.basic.dialog.EditDialog;
import com.android.ui.basic.dialog.ProgressDialog;
import com.android.common.FusionCode.Common;

/**
 * 提供基本方法的fragment基类<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public abstract class BasicFragment extends BaseFragment implements
        com.android.ui.basic.BasicCommonInterface
{
    private BasicCommonInterface mCommonInterface;
    
    /**
     * 进度显示框
     */
    private ProgressDialog mProDialog;
    
    /**
     * 页面是否进入pause状态
     */
    private boolean mIsPaused;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        mCommonInterface = new BasicCommonInterfaceImp(getActivity());
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
        mIsPaused = false;
    }
    
    @Override
    public void onPause()
    {
        super.onPause();
        mIsPaused = true;
    }
    
    /**
     * 从底部滑进 <BR>
     */
    public void slideInFromBottom()
    {
        mCommonInterface.slideInFromBottom();
    }
    
    /**
     * 从底部滑出 <BR>
     */
    public void slideOutToBottom()
    {
        mCommonInterface.slideOutToBottom();
    }
    
    /**
     * 从右边滑入 <BR>
     */
    public void slideInFromRight()
    {
        mCommonInterface.slideInFromRight();
    }
    
    /**
     * 从左边滑出 <BR>
     */
    public void slideOutFromLeft()
    {
        mCommonInterface.slideOutFromLeft();
    }
    
    /**
     * 获取shared preferences<BR>
     * @return SharedPreferences
     */
    public SharedPreferences getSharedPreferences()
    {
        return getActivity().getSharedPreferences(Common.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
    }
    
    /**
     * 是否为已经登录<BR>
     * @return 是否为已经登录
     */
    public boolean hasLogined()
    {
        return mCommonInterface.hasLogined();
    }
    
    /**
     * 弹编辑框<BR>
     * @param title 标题字符串
     * @param right 右按钮字符串
     * @param left 左按钮字符串
     * @param okListener  右按钮事件
     * @return 弹编辑框对象
     */
    @Override
    public EditDialog showEditDialog(String title, String left, String right,
            android.content.DialogInterface.OnClickListener okListener)
    {
        
        return mCommonInterface.showEditDialog(title, left, right, okListener);
    }
    
    /**
     * 显示进度框<BR>
     * @param message 对话框显示信息
     * @param cancelable 对话框可取消标志
     */
    private void showProgressDialog(String message, boolean cancelable)
    {
        if (mProDialog == null)
        {
            mProDialog = new ProgressDialog(getActivity(), cancelable);
        }
        mProDialog.setMessage(message);
        showProgressDialog(mProDialog);
    }
    
    /**
     * 弹出进度框<BR>
     * @param proDialog 对话框显示信息
     */
    protected void showProgressDialog(ProgressDialog proDialog)
    {
        if (!mIsPaused)
        {
            proDialog.show();
        }
    }
    
    protected boolean isPaused()
    {
        return mIsPaused;
    }
    
    /**
     * 显示进度框<BR>
     * @param message 对话框显示信息
     */
    protected void showProgressDialog(String message)
    {
        // 默认可取消当前对话框
        showProgressDialog(message, true);
    }
    
    /**
     * 显示进度框<BR>
     * @param msgResId 对话框显示信息
     */
    protected void showProgressDialog(int msgResId)
    {
        showProgressDialog(getResources().getString(msgResId), true);
    }
    
    /**
     * 显示进度框<BR>
     * @param msgResId 对话框显示信息
     * @param cancelable 是否可取消的标志
     */
    protected void showProgressDialog(int msgResId, boolean cancelable)
    {
        showProgressDialog(getResources().getString(msgResId), cancelable);
    }
    
    /**
     * 关闭进度框<BR>
     */
    protected void closeProgressDialog()
    {
        // 关闭ProgressDialog
        if (null != mProDialog)
        {
            mProDialog.dismiss();
            mProDialog = null;
        }
    }
    
    /**
     * 显示短时间的提示内容<BR>
     * @param content 提示内容
     */
    protected void showShortToast(String content)
    {
        Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
    }
    
    /**
     * 显示长时间的提示内容<BR>
     * @param content 提示内容
     */
    protected void showLongToast(String content)
    {
        Toast.makeText(getActivity(), content, Toast.LENGTH_LONG).show();
    }
}
