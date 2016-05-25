/*
 * 文件名: BasicActivity.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: UI 层基类Activity
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.ui.basic;

import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.component.log.Logger;
import com.android.framework.logic.BaseLogicBuilder;
import com.android.framework.ui.BaseActivity;
import com.android.framework.ui.LauncherActivity;
import com.android.logic.LogicBuilder;
import com.android.ui.basic.dialog.EditDialog;
import com.android.ui.basic.dialog.ProgressDialog;
import com.android.utils.CCImageLoader;
import com.android.utils.PreferencesUtil;
import com.android.common.FusionCode.Common;

/**
 * UI 层基类Activity<BR>
 * 包含UI 层的公用弹出框之类
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public abstract class BasicActivity extends LauncherActivity implements
        com.android.ui.basic.BasicCommonInterface
{

    /**
     * 当前的activity的对象实例
     */
    protected static BasicActivity mCurrentActivtiy;

    /**
     * TAG
     */
    private static final String TAG = "BasicActivity";

    /**
     * activity堆栈
     */
    private static Stack<Activity> mActivityStack = new Stack<Activity>();

    /**
     * 进度显示框
     */
    private ProgressDialog mProDialog;

    /**
     * 页面是否进入pause状态
     */
    private boolean mIsPaused;

    private BasicCommonInterface mCommonInterface;

    /**
     * 结束Activity<BR>
     * @see BaseActivity#finish()
     */
    @Override
    public void finish()
    {
        mActivityStack.remove(this);
        super.finish();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResume()
    {
        Logger.d(TAG, "BasictActivity " + this + " onResume");
        if (!isPrivateHandler())
        {
            mCurrentActivtiy = this;
        }
        mIsPaused = false;
        super.onResume();

    }

    /**
     * 启动<BR>
     * @param savedInstanceState Bundle
     * @see LauncherActivity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mCommonInterface = new BasicCommonInterfaceImp(this);
        if (needLogin() && !hasLogined())
        {
            finish();
            return;
        }
        mActivityStack.add(this);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPause()
    {
        Logger.d(TAG, "BasictActivity " + this + " onPause");
        mIsPaused = true;
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (null != imm && imm.isActive())
        {
            if (null != this.getCurrentFocus()
                    && null != this.getCurrentFocus().getWindowToken())
            {
                imm.hideSoftInputFromWindow(this.getCurrentFocus()
                        .getApplicationWindowToken(), 0);
            }
        }
        
    }
    
    /**
     * 当前Activity是否处于暂停状态<BR>
     * @return 是否处于暂停状态
     */
    protected boolean isActivityPause()
    {
        return mIsPaused;
    }
    
    /**
     * 销毁 当前Activity置空<BR>
     * @see com.paitao.basic.android.framework.ui.BaseActivity#onDestroy()
     */
    @Override
    protected void onDestroy()
    {
        closeProgressDialog();
        super.onDestroy();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void initSystem(Context context)
    {
        //日志
        Logger.configureLogback(context);
        //        LogReporter.setLogDir(Logger.getLogPath(context));
        //        RpcUtils.setDebug(FusionConfig.getInstance().isDebug());
        
        //最早初始化
        PreferencesUtil.initContext(context);
        
        //做一些系统初始化的工作 
        CCImageLoader.getInstance().init(context);
        //取出是否有登陆的sessionid
        initResoure();
        //给服务器做回执用的 
        initDelegateCenter(context);
        initServerAddr();
        //        LogReporter.reportLater();
    }
    
    /**
     * 初始化资源数据时候得到当前的sessionid 和userid<BR>
     */
    private void initResoure()
    {
        //        FusionConfig.getInstance()
        //                .setSessionId(PreferencesUtil.getAttrString(Common.KEY_SESSION_ID));
        //        FusionConfig.getInstance()
        //                .setUid(PreferencesUtil.getAttrLong(Common.KEY_USER_ID));
        
    }
    
    /**
     * 初始化服务器地址<BR>
     */
    private void initServerAddr()
    {
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected BaseLogicBuilder createLogicBuilder(Context context)
    {
        BaseLogicBuilder builder = LogicBuilder.getInstance(context);
        mCurrentActivtiy = this;
        
        return builder;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void initLogics()
    {
        
    }
    
    /**
     * 获取安全的context，防止badtoken异常<BR>
     * @return context
     */
    protected Context getContext()
    {
        Context context = null;
        if (null != getParent())
        {
            context = getParent();
        }
        else
        {
            context = this;
        }
        return context;
    }
    
    /**
     * 返回一个boolean表示展示该页面是否需要登录成功
     * @return boolean 是否是登录后的页面
     */
    protected boolean needLogin()
    {
        return true;
    }
    
    /**
     * 当焦点停留在view上时，隐藏输入法栏
     * @param view view
     */
    protected void hideInputWindow(View view)
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        
        if (null != imm && null != view)
        {
            imm.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    
    /**
     * 当焦点停留在view上时，显示输入法栏
     * @param view view
     */
    protected void showInputWindow(View view)
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        
        if (null != imm && null != view)
        {
            imm.showSoftInput(view, 0);
        }
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
            mProDialog = new ProgressDialog(getContext(), cancelable);
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
        return getSharedPreferences(Common.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
    }
    
    /**
     * 是否为已经登录<BR>
     * @return sessionid 判断 是否为已经登录
     */
    public boolean hasLogined()
    {
        return mCommonInterface.hasLogined();
    }
    
    /**
     * 设置已经成功登录<BR>
     * @param sessionid 登陆后纪录sessionid来判断是否登陆
     */
    protected void setLogined(String sessionid)
    {
        PreferencesUtil.setAttr(Common.KEY_SESSION_ID, sessionid);
    }
    
    private void initDelegateCenter(Context context)
    {
        final String dataDir = context.getFilesDir().getPath();
        //        RpcDelegateCenter.singleton().setDelegate(new RpcDelegate()
        //        {
        //            
        //            @Override
        //            public String getSesionKey()
        //            {
        //                // TODO Auto-generated method stub
        //                return FusionConfig.getInstance().getSessionId();
        //            }
        //            
        //            @Override
        //            public int getClientVersion()
        //            {
        //                // TODO Auto-generated method stub
        //                return 0;
        //            }
        //            
        //            @Override
        //            public String getUid()
        //            {
        //                
        //                if (!TextUtils.isEmpty(FusionConfig.getInstance()
        //                        .getSessionId()))
        //                {
        //                    return FusionConfig.getInstance().getUid() + "@cici";
        //                }
        //                return null;
        //            }
        //            
        //            @Override
        //            public String getAcceptLanguage()
        //            {
        //                // TODO Auto-generated method stub
        //                return null;
        //            }
        //            
        //            @Override
        //            public void onRpcKickOut()
        //            {
        //                // TODO 退出登陆
        //                
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
    
    /**
     * 显示短时间的提示内容<BR>
     * @param resId 提示内容
     */
    protected void showShortToast(int resId)
    {
        showShortToast(getString(resId));
    }
    
    /**
     * 显示短时间的提示内容<BR>
     * @param content 提示内容
     */
    protected void showShortToast(String content)
    {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }
    
    /**
     * 显示长时间的提示内容<BR>
     * @param content 提示内容
     */
    protected void showLongToast(String content)
    {
        Toast.makeText(this, content, Toast.LENGTH_LONG).show();
    }
    
    /**
     * 显示长时间的提示内容<BR>
     * @param id 提示内容的stringid
     */
    protected void showLongToast(int id)
    {
        Toast.makeText(this, id, Toast.LENGTH_LONG).show();
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
    
}
