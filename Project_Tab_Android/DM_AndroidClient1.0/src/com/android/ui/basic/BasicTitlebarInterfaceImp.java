/*
 * 文件名: BasicTitlebarInterface.java
 * 版    权： Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 定义公共的titleBar方法
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.ui.basic;

import android.view.View;
import android.view.View.OnClickListener;

import com.android.ui.basic.titlebar.TitleBar;
import com.android.ui.basic.titlebar.TitleBar.TitleBarStyle;

/**
 * 定义公共的titleBar方法<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class BasicTitlebarInterfaceImp implements BasicTitlebarInterface
{
    
    private TitleBar mTitleBar;
    
    /**
     * 构造方法
     * @param titleBar TitleBar对象
     */
    public BasicTitlebarInterfaceImp(TitleBar titleBar)
    {
        mTitleBar = titleBar;
    }
    
    @Override
    public void setLeftButtonListener(OnClickListener listener)
    {
        mTitleBar.setLeftButtonClickListener(listener);
    }
    
    @Override
    public void setRightButtonFirstListener(OnClickListener listener)
    {
        mTitleBar.setRightButtonFirstClickListener(listener);
    }
    
    @Override
    public void setRightButtonSecondListener(OnClickListener listener)
    {
        mTitleBar.setRightButtonSecondClickListener(listener);
    }
    
    @Override
    public int getLayoutId()
    {
        return 0;
    }
    
    @Override
    public void setLeftTitleButton(int ldrawableId, OnClickListener listener)
    {
        if (0 == ldrawableId)
        {
            mTitleBar.setLeftButtonVisible(false);
            return;
        }
        mTitleBar.setLeftButtonVisible(true);
        if (0 != ldrawableId)
        {
            mTitleBar.setLeftButtonDrawable(ldrawableId);
        }
        if (null != listener)
        {
            mTitleBar.getLeftButton().setOnClickListener(listener);
        }
    }
    
    @Override
    public void setLeftSubTitle(CharSequence subTitle, OnClickListener listener)
    {
        mTitleBar.setLeftSubTitle(subTitle);
        mTitleBar.setLeftSubtTitleListener(listener);
    }
    
    @Override
    public void setLeftSubTitle(CharSequence subTitle)
    {
        mTitleBar.setLeftSubTitle(subTitle);
    }
    
    @Override
    public void setRightButtonFirst(int rdrawableId, OnClickListener listener)
    {
        if (0 == rdrawableId)
        {
            mTitleBar.setRightButtonFirstVisible(false);
            return;
        }
        mTitleBar.setRightButtonFirstVisible(true);
        if (0 != rdrawableId)
        {
            mTitleBar.setRightButtonFirstDrawable(rdrawableId);
        }
        if (null != listener)
        {
            mTitleBar.getRightButtonFirst().setOnClickListener(listener);
        }
    }
    
    @Override
    public void setRightButtonSecond(int rdrawableId, OnClickListener listener)
    {
        if (0 == rdrawableId)
        {
            mTitleBar.setRightButtonSecondVisible(false);
            return;
        }
        mTitleBar.setRightButtonSecondVisible(true);
        if (0 != rdrawableId)
        {
            mTitleBar.setRightButtonSecondDrawable(rdrawableId);
        }
        if (null != listener)
        {
            mTitleBar.getRightButtonSecond().setOnClickListener(listener);
        }
        
    }
    
    @Override
    public void setMiddleTitle(int titleId)
    {
        mTitleBar.setTitleText(titleId);
    }
    
    @Override
    public void setMiddleTitle(CharSequence titleStr)
    {
        mTitleBar.setTitleText(titleStr);
    }
    
    @Override
    public void setTitle(int texttitle, int ldrawableId, int rdrawableIdFirst,
            int rdrawableIdSecond)
    {
        mTitleBar.setTitle(texttitle,
                ldrawableId,
                rdrawableIdFirst,
                rdrawableIdSecond);
    }
    
    @Override
    public void setPageCount(int count)
    {
        mTitleBar.setPageTotalCount(count);
    }
    
    @Override
    public void setCurrentPage(int currentPage)
    {
        mTitleBar.setCurrentPage(currentPage);
    }
    
    @Override
    public void showPageNumberView(boolean show)
    {
        mTitleBar.showNumberView(show);
    }
    
    @Override
    public void showPageIndicatorView(boolean show)
    {
        mTitleBar.showIndicatorView(show);
    }
    
    @Override
    public boolean initializeTitlBar()
    {
        return false;
    }
    
    @Override
    public TitleBarStyle getTitleBarStyle()
    {
        return TitleBarStyle.NORMAL;
    }
    
    @Override
    public int getContentContainerBgId()
    {
        return -1;
    }
    
    @Override
    public void setTitleBarBackground(int backgroundId)
    {
        mTitleBar.setBackgroundResource(backgroundId);
    }
    
    @Override
    public void setTitleBarVisible(boolean isVisible)
    {
        mTitleBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
    
    @Override
    public boolean getTitleBarVisible()
    {
        return mTitleBar.getVisibility() == View.VISIBLE ? true : false;
    }
    
    @Override
    public void setMiddleTitleDrawable(int drawableId)
    {
        mTitleBar.setTitleDrawable(drawableId);
    }
    
    @Override
    public void setRightButtonFirstVisible(boolean visible)
    {
        mTitleBar.setRightButtonFirstVisible(visible);
    }
    
    @Override
    public void setRightButtonSecondVisible(boolean visible)
    {
        mTitleBar.setRightButtonSecondVisible(visible);
    }
    
}
