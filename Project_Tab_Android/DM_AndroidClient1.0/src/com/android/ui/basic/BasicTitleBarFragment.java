/*
 * 文件名: BasicTitleBarFragment.java
 * 版    权： Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 包含标题栏的fragment基类
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.ui.basic;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.android.ui.basic.titlebar.DefaultView;
import com.android.ui.basic.titlebar.TitleBar;
import com.android.R;
import com.android.common.FusionAction.LoginAction;
import com.android.ui.basic.titlebar.TitleBar.TitleBarStyle;

/**
 * 包含标题栏的fragment基类<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public abstract class BasicTitleBarFragment extends BasicFragment implements
        BasicTitlebarInterface
{
    
    /**
     * TitleBar
     */
    private TitleBar mTitleBar;
    
    /**
     * 子视图的容器
     */
    private LinearLayout mContentContainer;
    
    /**
     * 根视图
     */
    private View mRootView = null;
    
    /**
     * 默认视图 在未登录的时候展示
     */
    private DefaultView mDefaultView;
    
    /**
     * 登录后显示的真实数据视图
     */
    private View mContentView;
    
    private BasicTitlebarInterface mTitlebarInterface;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = inflater.inflate(R.layout.content_container, null);
        
        initViews(inflater);
        
        mContentContainer = (LinearLayout) mRootView.findViewById(R.id.content_container);
        
        initContainer(inflater);
        initializeViews(mRootView);
        return mRootView;
    }
    
    private void initContainer(LayoutInflater inflater)
    {
        
        mContentView = inflater.inflate(getLayoutId(), null);
        mContentContainer.addView(mContentView,
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        mDefaultView = (DefaultView) mRootView.findViewById(R.id.default_view);
        if (!hasLogined() && needLogin())
        {
            showDefaultView();
        }
        else
        {
            showContentView();
        }
    }
    
    /**
     * 界面初始化 <BR>
     */
    private void initViews(LayoutInflater inflater)
    {
        TitleBarStyle style = getTitleBarStyle();
        switch (style)
        {
            case NORMAL:
                mRootView = inflater.inflate(R.layout.content_container, null);
                break;
            case TRANPARENT:
                mRootView = inflater.inflate(R.layout.content_container_trans,
                        null);
                break;
            default:
                mRootView = inflater.inflate(R.layout.content_container, null);
                break;
        }
        mTitleBar = (TitleBar) mRootView.findViewById(R.id.title_bar_layout);
        mTitlebarInterface = new BasicTitlebarInterfaceImp(mTitleBar);
        if (!initializeTitlBar())
        {
            mTitleBar.hideTitleBar();
            return;
        }
    }
    
    /**
     * 标题左按钮对象 <BR>
     * @param listener OnClickListener
     */
    public void setLeftButtonListener(OnClickListener listener)
    {
        mTitlebarInterface.setLeftButtonListener(listener);
    }
    
    /**
     * 标题右边第一个按钮对象 <BR>
     * @param listener OnClickListener
     */
    public void setRightButtonFirstListener(OnClickListener listener)
    {
        mTitlebarInterface.setRightButtonFirstListener(listener);
    }
    
    /**
     * 标题右边第一个按钮对象 <BR>
     * @param listener OnClickListener
     */
    public void setRightButtonSecondListener(OnClickListener listener)
    {
        mTitlebarInterface.setRightButtonSecondListener(listener);
    }
    
    /**
     * 获得子Activity的布局ID
     * @return 子Activity的布局ID
     */
    public abstract int getLayoutId();
    
    /**
     * 设置左button <BR>
     * @param ldrawableId 左标题图片
     * @param listener 左标题事件
     */
    public void setLeftTitleButton(int ldrawableId, OnClickListener listener)
    {
        mTitlebarInterface.setLeftTitleButton(ldrawableId, listener);
    }
    
    /**
     * 设置右标题 <BR>
     * @param rdrawableId 右标题图片
     * @param listener 右标题事件
     */
    public void setRightButtonFirst(int rdrawableId, OnClickListener listener)
    {
        mTitlebarInterface.setRightButtonFirst(rdrawableId, listener);
    }
    
    /**
     * 设置右标题 <BR>
     * @param rdrawableId 右标题图片
     * @param listener 右标题事件
     */
    public void setRightSecondButton(int rdrawableId, OnClickListener listener)
    {
        mTitlebarInterface.setRightButtonSecond(rdrawableId, listener);
    }
    
    /**
     * 
     * 设置中间的标题 <BR>
     * @param titleId 文字资源ID
     */
    public void setMiddleTitle(int titleId)
    {
        mTitlebarInterface.setMiddleTitle(titleId);
    }
    
    /**
     * 设置中间的标题 <BR>
     * @param titleStr 文字字符串
     */
    public void setMiddleTitle(CharSequence titleStr)
    {
        mTitlebarInterface.setMiddleTitle(titleStr);
    }
    
    /**
     * 设置标题<BR>
     * @param texttitle 中间
     * @param ldrawableId 左边图片
     * @param rdrawableIdFirst 右边第一个button图片
     * @param rdrawableIdSecond 右边第二个button图片
     */
    public void setTitle(int texttitle, int ldrawableId, int rdrawableIdFirst,
            int rdrawableIdSecond)
    {
        mTitlebarInterface.setTitle(texttitle,
                ldrawableId,
                rdrawableIdFirst,
                rdrawableIdSecond);
    }
    
    /**
     * 登录成功后显示真实的view<BR>
     */
    public void updateViewAfterLogin()
    {
        showContentView();
    }
    
    /**
     * 登录成功后显示真实的view<BR>
     */
    public void updateViewAfterLoginOut()
    {
        showDefaultView();
    }
    
    /**
     * 隐藏按钮<BR>
     */
    protected void hideButton()
    {
        mTitleBar.setLeftButtonVisible(false);
        mTitleBar.setRightButtonFirstVisible(false);
        mTitleBar.setRightButtonSecondVisible(false);
    }
    
    /**
     * 初始化titleBar<BR>
     * 子类需要在该方法中调用设置按钮标题的方法，并且需要返回true才会显示标题栏
     * @return 是否显示titleBar
     */
    public abstract boolean initializeTitlBar();
    
    /**
     * 获得标题栏类型<BR>
     * @return 标题栏类型
     */
    public TitleBarStyle getTitleBarStyle()
    {
        return TitleBarStyle.NORMAL;
    }
    
    /**
     * 未登录的时候是否需要登录显示此界面<BR>
     * @return 是否需要展示
     */
    protected boolean needLogin()
    {
        return true;
    }
    
    /**
     * 处理back事件的点击
     */
    protected void onBackClick()
    {
    }
    
    /**
     * 初始化具体页面的视图<BR>
     * @param rootView 根布局
     */
    protected abstract void initializeViews(View rootView);
    
    private void showDefaultView()
    {
        mDefaultView.setVisibility(View.VISIBLE);
        mContentView.setVisibility(View.GONE);
        mDefaultView.setLinkClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginAction.ACTION);
                startActivity(intent);
                slideInFromBottom();
            }
        });
    }
    
    private void showContentView()
    {
        mDefaultView.setVisibility(View.GONE);
        mContentView.setVisibility(View.VISIBLE);
    }
    
    @Override
    public void setRightButtonSecond(int rdrawableId, OnClickListener listener)
    {
        mTitlebarInterface.setRightButtonSecond(rdrawableId, listener);
    }
    
    @Override
    public void setPageCount(int count)
    {
        mTitlebarInterface.setPageCount(count);
    }
    
    @Override
    public void setCurrentPage(int currentPage)
    {
        mTitlebarInterface.setCurrentPage(currentPage);
    }
    
    @Override
    public void showPageNumberView(boolean show)
    {
        mTitlebarInterface.showPageNumberView(show);
    }
    
    @Override
    public void showPageIndicatorView(boolean show)
    {
        mTitlebarInterface.showPageIndicatorView(show);
    }
    
    @Override
    public int getContentContainerBgId()
    {
        return 0;
    }
    
    @Override
    public void setTitleBarBackground(int backgroundId)
    {
        mTitlebarInterface.setTitleBarBackground(backgroundId);
    }
    
    @Override
    public void setTitleBarVisible(boolean isVisible)
    {
        mTitlebarInterface.setTitleBarVisible(isVisible);
    }
    
    @Override
    public boolean getTitleBarVisible()
    {
        return mTitlebarInterface.getTitleBarVisible();
    }
    
    @Override
    public void setLeftSubTitle(CharSequence subTitle, OnClickListener listener)
    {
        mTitlebarInterface.setLeftSubTitle(subTitle, listener);
        
    }
    
    @Override
    public void setMiddleTitleDrawable(int drawableId)
    {
        mTitlebarInterface.setMiddleTitleDrawable(drawableId);
    }
    
    @Override
    public void setLeftSubTitle(CharSequence subTitle)
    {
        mTitlebarInterface.setLeftSubTitle(subTitle);
    }
    
    @Override
    public void setRightButtonFirstVisible(boolean visible)
    {
        mTitlebarInterface.setRightButtonFirstVisible(visible);
    }
    
    @Override
    public void setRightButtonSecondVisible(boolean visible)
    {
        mTitlebarInterface.setRightButtonSecondVisible(visible);
    }
    
    @Override
    protected void initLogics()
    {
        // TODO Auto-generated method stub
        
    }
}
