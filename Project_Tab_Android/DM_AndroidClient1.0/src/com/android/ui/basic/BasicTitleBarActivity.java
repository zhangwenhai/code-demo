/*
 * 文件名: BasicTitleBarActivity.java
 * 版    权： Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 包含titleBar的activity基类
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.ui.basic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.android.ui.basic.titlebar.TitleBar;
import com.android.R;
import com.android.ui.basic.titlebar.TitleBar.TitleBarStyle;

/**
 * 包含titleBar的activity基类<BR>
 * 子类无需调用setContentView方法设置视图，必须实现getLayoutId方法传入activity所需要战士的布局文件
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public abstract class BasicTitleBarActivity extends BasicActivity implements
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

    private BasicTitlebarInterface mTitlebarInterface;

    /**
     * 启动 <BR>
     * @param savedInstanceState Bundle
     * @see BasicActivity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        
        initTitlebarViews();
        
        mContentContainer = (LinearLayout) findViewById(R.id.content_container);
        if (getContentContainerBgId() != -1)
        {
            mContentContainer.setBackgroundResource(getContentContainerBgId());
        }
        
        View contentView = inflater.inflate(getLayoutId(), null);
        mContentContainer.addView(contentView,
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
    }
    
    /**
     * 标题栏初始化 <BR>
     */
    private void initTitlebarViews()
    {
        TitleBarStyle style = getTitleBarStyle();
        switch (style)
        {
            case NORMAL:
                setContentView(R.layout.content_container);
                break;
            case TRANPARENT:
                setContentView(R.layout.content_container_trans);
                break;
            default:
                setContentView(R.layout.content_container);
                break;
        }
        mTitleBar = (TitleBar) findViewById(R.id.title_bar_layout);
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
    public void setRightButtonSecond(int rdrawableId, OnClickListener listener)
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
     * 设置titlebar的总页数<BR>
     * @param count 总页数
     */
    public void setPageCount(int count)
    {
        mTitlebarInterface.setPageCount(count);
    }
    
    /**
     * 设置titleBar的当前页<BR>
     * @param currentPage 当前页
     */
    public void setCurrentPage(int currentPage)
    {
        mTitlebarInterface.setCurrentPage(currentPage);
    }
    
    /**
     * 设置是否显示数字 currentPage\totalCount 例如 1\5<BR>
     * @param show 是否显示
     */
    public void showPageNumberView(boolean show)
    {
        mTitlebarInterface.showPageNumberView(show);
    }
    
    /**
     * 是否显示指示器<BR>
     * @param show 是否显示
     */
    public void showPageIndicatorView(boolean show)
    {
        mTitlebarInterface.showPageIndicatorView(show);
    }
    
    /**
     * 初始化titleBar<BR>
     * 子类需要在该方法中调用设置按钮标题的方法，并且需要返回true才会显示标题栏
     * @return 是否显示titleBar
     */
    public abstract boolean initializeTitlBar();
    
    /**
     * 获得标题栏类型，子类根据需要重写此方法展示不同样式标题栏<BR>
     * @return 标题栏类型
     */
    public TitleBarStyle getTitleBarStyle()
    {
        return TitleBarStyle.NORMAL;
    }
    
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
    
    public int getContentContainerBgId()
    {
        return -1;
    }
    
    /**
     * 设置标题栏背景<BR>
     * @param backgroundId 背景ID
     */
    public void setTitleBarBackground(int backgroundId)
    {
        mTitlebarInterface.setTitleBarBackground(backgroundId);
    }
    
    /**
     * 
     * 设置标题栏是否可见<BR>
     * @param isVisible 是否可见
     */
    public void setTitleBarVisible(boolean isVisible)
    {
        mTitlebarInterface.setTitleBarVisible(isVisible);
    }
    
    /**
     * 
     * 获取标题是否可见<BR>
     * @return 返回标题是否可见
     */
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
    
    /**
     * 获取标题<BR>
     * @return 返回标题的View
     */
    protected TitleBar getTitleBar()
    {
        return mTitleBar;
    }
}
