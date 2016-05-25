/*
 * 文件名: DefaultView.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 自定义标题栏
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.R;

/**
 * 自定义标题栏<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class TitleBar extends LinearLayout
{
    /**
     * 标题栏左侧按钮
     */
    private ImageView mLeftIconButton;
    
    /**
     * 标题栏左侧标题
     */
    private Button mLeftSubTitleButton;
    
    /**
     * 标题栏右边第一个按钮
     */
    private ImageView mRightIconButtonFirst;
    
    /**
     * 标题栏右边第二个按钮
     */
    private ImageView mRightIconButtonSecond;
    
    /**
     * 标题栏上方的pageView
     */
    private TitleBarPageView mPageLayout;
    
    /**
     * 标题栏
     */
    private TextView mTitleView;
    
    /**
     * TitleBar的总体布局
     */
    private View mTitlebarLayout;
    
    /**
     * 标题栏
     * @param context Context
     * @param attrs AttributeSet
     */
    public TitleBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTitlebarLayout = inflater.inflate(R.layout.titlebar_base, null);
        setOrientation(LinearLayout.VERTICAL);
        LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(mTitlebarLayout, layoutParams);
        init();
    }
    
    /**
     * 不显示标题栏
     */
    public void hideTitleBar()
    {
        setVisibility(View.GONE);
    }
    
    /**
     * 显示标题栏
     */
    public void showTitleBar()
    {
        setVisibility(View.VISIBLE);
    }
    
    /**
     * 设置标题<BR>
     * @param texttitle 中间
     * @param ldrawableId 左边图片
     * @param rFirstDrawableId 右边第一个button图片
     * @param rSecondDrawableId 右边第二个button图片
     */
    public void setTitle(int texttitle, int ldrawableId, int rFirstDrawableId,
            int rSecondDrawableId)
    {
        setTitleText(texttitle);
        setLeftButtonDrawable(ldrawableId);
        setRightButtonFirstDrawable(rFirstDrawableId);
        setRightButtonSecondDrawable(rSecondDrawableId);
    }
    
    /**
     * 设置标题为图片<BR>
     * @param drawableId 图片id
     */
    public void setTitleDrawable(int drawableId)
    {
        mTitleView.setVisibility(View.VISIBLE);
        mTitleView.setBackgroundResource(drawableId);
    }
    
    /**
     * 设置左边button图片<BR>
     * @param drawableId 图片资源ID
     */
    public void setLeftButtonDrawable(int drawableId)
    {
        mLeftIconButton.setImageResource(drawableId);
    }
    
    /**
     * 设置title文字<BR>
     * @param titleId 文字资源ID
     */
    public void setTitleText(int titleId)
    {
        mTitleView.setText(titleId);
    }
    
    /**
     * 设置title文字<BR>
     * @param titleStr 文字字符串
     */
    public void setTitleText(CharSequence titleStr)
    {
        mTitleView.setText(titleStr);
    }
    
    /**
     * 设置右边第一个button图片<BR>
     * @param drawableId 图片资源ID
     */
    public void setRightButtonFirstDrawable(int drawableId)
    {
        mRightIconButtonFirst.setImageResource(drawableId);
    }
    
    /**
     * 设置右边第二个button图片<BR>
     * @param drawableId 图片资源ID
     */
    public void setRightButtonSecondDrawable(int drawableId)
    {
        mRightIconButtonSecond.setImageResource(drawableId);
    }
    
    /**
     * 获得左边button<BR>
     * @return 左边button
     */
    public ImageView getLeftButton()
    {
        return mLeftIconButton;
    }
    
    /**
     * 获得左边副标题的对象<BR>
     * @return 左边副标题的对象
     */
    public Button getLeftSubTitleBtn()
    {
        return mLeftSubTitleButton;
    }
    
    /**
     * 获得右边第二个button<BR>
     * @return 右边button
     */
    public ImageView getRightButtonSecond()
    {
        return mRightIconButtonSecond;
    }
    
    /**
     * 获得右边第一个button<BR>
     * @return 右边button
     */
    public ImageView getRightButtonFirst()
    {
        return mRightIconButtonFirst;
    }
    
    /**
     * 设置左边button事件监听<BR>
     * @param listener OnClickListener
     */
    public void setLeftButtonClickListener(OnClickListener listener)
    {
        mLeftIconButton.setOnClickListener(listener);
    }
    
    /**
     * 设置右边button事件监听<BR>
     * @param listener OnClickListener
     */
    public void setRightButtonFirstClickListener(OnClickListener listener)
    {
        mRightIconButtonFirst.setOnClickListener(listener);
    }
    
    /**
     * 设置右边button事件监听<BR>
     * @param listener OnClickListener
     */
    public void setRightButtonSecondClickListener(OnClickListener listener)
    {
        mRightIconButtonSecond.setOnClickListener(listener);
    }
    
    /**
     * 设置右边第一个button是否显示<BR>
     * @param show 是否显示
     */
    public void setRightButtonFirstVisible(boolean show)
    {
        mRightIconButtonFirst.setVisibility(show ? View.VISIBLE : View.GONE);
    }
    
    /**
     * 设置右边第二个button是否显示<BR>
     * @param show 是否显示
     */
    public void setRightButtonSecondVisible(boolean show)
    {
        mRightIconButtonSecond.setVisibility(show ? VISIBLE : GONE);
    }
    
    /**
     * 设置左边button是否显示<BR>
     * @param show 是否显示
     */
    public void setLeftButtonVisible(boolean show)
    {
        mLeftIconButton.setVisibility(show ? VISIBLE : GONE);
    }
    
    /**
     * 设置左边副标题是否显示<BR>
     * @param show 是否显示
     */
    public void setLeftSubtTitleVisible(boolean show)
    {
        mLeftSubTitleButton.setVisibility(show ? VISIBLE : GONE);
    }
    
    /**
     * 设置分页视图是否展示<BR>
     * @param show 是否显示
     */
    public void setPageVisible(boolean show)
    {
        mPageLayout.setVisibility(show ? VISIBLE : GONE);
        if (show)
        {
            mTitleView.setVisibility(GONE);
        }
    }
    
    /**
     * 设置总数<BR>
     * @param count 总数
     */
    public void setPageTotalCount(int count)
    {
        setPageVisible(true);
        mPageLayout.setTotalPage(count);
    }
    
    /**
     * 设置当前页<BR>
     * @param currentPage 当前页
     */
    public void setCurrentPage(int currentPage)
    {
        mPageLayout.setCurrentPage(currentPage);
    }
    
    /**
     * 设置是否显示数字 currentPage\totalCount 例如 1\5<BR>
     * @param show 是否显示
     */
    public void showNumberView(boolean show)
    {
        mPageLayout.showNumberView(show);
    }
    
    /**
     * 是否显示指示器<BR>
     * @param show 是否显示
     */
    public void showIndicatorView(boolean show)
    {
        mPageLayout.showIndicatorView(show);
    }
    
    /**
     * 设置titleBar类型<BR>
     * @param style titleBar类型
     */
    public void setTitleBarStyle(TitleBarStyle style)
    {
        style.setStyleForTitleBar(mTitlebarLayout,
                mLeftIconButton,
                mRightIconButtonFirst,
                mTitleView);
    }
    
    /**
     * 设置左侧副标题文字<BR>
     * @param subtTitle 文字
     */
    public void setLeftSubTitle(CharSequence subtTitle)
    {
        mLeftSubTitleButton.setVisibility(View.VISIBLE);
        mLeftSubTitleButton.setText(subtTitle);
    }
    
    /**
     * 设置左侧副标题监听<BR>
     * @param listener 监听
     */
    public void setLeftSubtTitleListener(OnClickListener listener)
    {
        mLeftSubTitleButton.setOnClickListener(listener);
    }
    
    private void init()
    {
        mLeftIconButton = (ImageView) findViewById(R.id.titlebar_btn_left);
        mLeftSubTitleButton = (Button) findViewById(R.id.titlebar_subtitle_left);
        mRightIconButtonFirst = (ImageView) findViewById(R.id.titlebar_btn_right_first);
        mRightIconButtonSecond = (ImageView) findViewById(R.id.titlebar_btn_right_second);
        mTitleView = (TextView) findViewById(R.id.titlebar_text_title);
        mPageLayout = (TitleBarPageView) findViewById(R.id.titlebar_page);
    }
    
    /**
     * 标题栏类型<BR>
     * @author zhaozeyang
     * @version [Paitao Client V20130911, 2013-9-29]
     */
    public enum TitleBarStyle
    {
        /**
         * 普通
         */
        NORMAL
        {
            @Override
            void setStyleForTitleBar(View titleBar, ImageView leftButton,
                    ImageView rightButton, TextView titleView)
            {
                titleBar.setBackgroundResource(R.drawable.bg_titlebar_repeat);
            }
        },
        /**
         * 透明
         */
        TRANPARENT
        {
            @Override
            void setStyleForTitleBar(View titleBar, ImageView leftButton,
                    ImageView rightButton, TextView titleView)
            {
                titleBar.setBackgroundResource(android.R.color.transparent);
                leftButton.setBackgroundResource(android.R.color.transparent);
                rightButton.setBackgroundResource(android.R.color.transparent);
            }
        };
        abstract void setStyleForTitleBar(View titleBar, ImageView leftButton,
                ImageView rightButton, TextView titleView);
    }
}
