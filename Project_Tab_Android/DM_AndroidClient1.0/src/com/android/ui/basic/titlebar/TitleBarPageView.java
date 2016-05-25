/*
 * 文件名: TitleBarPageView.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 显示分页页码和指示器的视图
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.ui.basic.titlebar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.component.log.Logger;
import com.android.R;

/**
 * 显示分页页码和指示器的视图<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class TitleBarPageView extends RelativeLayout
{
    /**
     * TAG
     */
    private static final String TAG = "PageLayout";
    
    /**
     * 指示器的间距
     */
    private static final int INDICATOR_MARGIN = 10;
    
    /**
     * 显示页数的视图
     */
    private TextView mNumberView;
    
    /**
     * 显示指示器的布局
     */
    private LinearLayout mIndicatorLayout;
    
    /**
     * 总页数
     */
    private int mTotalPage;
    
    /**
     * 当前页
     */
    private int mCurrentPage = 0;
    
    /**
     * 是否展示页数
     */
    private boolean mShowNumberView = true;
    
    /**
     * 是否展示指示器
     */
    private boolean mShowIndicatorView = true;
    
    /**
     * 构造方法
     * @param context 上下文
     * @param attrs 属性
     */
    public TitleBarPageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        intViews();
    }
    
    /**
     * 设置总页数<BR>
     * @param totalPage 总页数
     */
    public void setTotalPage(int totalPage)
    {
        mTotalPage = totalPage;
        intPageLayout();
    }
    
    /**
     * 设置当前页<BR>
     * @param currentPage 当前页
     */
    public void setCurrentPage(int currentPage)
    {
        mCurrentPage = currentPage;
        updateView();
    }
    
    /**
     * 是否展示页数<BR>
     * @param show 是否展示
     */
    public void showNumberView(boolean show)
    {
        mShowNumberView = show;
        mNumberView.setVisibility(mShowNumberView ? VISIBLE : GONE);
    }
    
    /**
     * 是否展示指示器<BR>
     * @param show 是否展示
     */
    public void showIndicatorView(boolean show)
    {
        mShowIndicatorView = show;
        mIndicatorLayout.setVisibility(mShowIndicatorView ? VISIBLE : GONE);
    }
    
    private void intViews()
    {
        View rootView = LayoutInflater.from(getContext())
                .inflate(R.layout.titlebar_page_layout, null);
        mNumberView = (TextView) rootView.findViewById(R.id.page_number);
        mIndicatorLayout = (LinearLayout) rootView.findViewById(R.id.page_indicator);
        addView(rootView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }
    
    /**
     * 更新页数和指示器<BR>
     */
    private void updateView()
    {
        mNumberView.setText((mCurrentPage + 1) + "/" + mTotalPage);
        updateIndicator();
    }
    
    private void updateIndicator()
    {
        for (int i = 0; i < mTotalPage; i++)
        {
            if (i == mCurrentPage)
            {
                ((ImageView) mIndicatorLayout.getChildAt(i)).setSelected(true);
                Logger.d(TAG, "currentPage is  " + mCurrentPage);
            }
            else
            {
                ((ImageView) mIndicatorLayout.getChildAt(i)).setSelected(false);
            }
        }
    }
    
    private void intPageLayout()
    {
        mIndicatorLayout.removeAllViews();
        mNumberView.setText((mCurrentPage + 1) + "/" + mTotalPage);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.rightMargin = INDICATOR_MARGIN;
        for (int i = 0; i < mTotalPage; i++)
        {
            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundResource(R.drawable.page_indicator_selector);
            if (i == mCurrentPage)
            {
                imageView.setSelected(true);
            }
            else
            {
                imageView.setSelected(false);
            }
            mIndicatorLayout.addView(imageView, lp);
        }
    }
}
