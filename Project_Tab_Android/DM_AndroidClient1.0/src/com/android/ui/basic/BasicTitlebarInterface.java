/*
 * 文件名: BasicTitlebarInterface.java
 * 版    权： Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 提供公共的titlebar方法
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.ui.basic;

import android.view.View.OnClickListener;

import com.android.ui.basic.titlebar.TitleBar.TitleBarStyle;

/**
 * 提供公共的titlebar方法<BR>
 * @author zhaozeyang
 * @version [Paitao Client V20130911, 2013-11-12] 
 */
public interface BasicTitlebarInterface
{
    
    /**
     * 标题左按钮对象 <BR>
     * @param listener OnClickListener
     */
    void setLeftButtonListener(OnClickListener listener);
    
    /**
     * 标题右边第一个按钮对象 <BR>
     * @param listener OnClickListener
     */
    void setRightButtonFirstListener(OnClickListener listener);
    
    /**
     * 标题右边第一个按钮对象 <BR>
     * @param listener OnClickListener
     */
    void setRightButtonSecondListener(OnClickListener listener);
    
    /**
     * 设置右侧副标题<BR>
     * @param subTitle 标题文字
     * @param listener 标题点击事件
     */
    void setLeftSubTitle(CharSequence subTitle, OnClickListener listener);
    
    /**
     * 设置左侧副标题<BR>
     * @param subTitle 副标题
     */
    void setLeftSubTitle(CharSequence subTitle);
    
    /**
     * 获得子Activity的布局ID
     * @return 子Activity的布局ID
     */
    abstract int getLayoutId();
    
    /**
     * 设置左button <BR>
     * @param ldrawableId 左标题图片
     * @param listener 左标题事件
     */
    void setLeftTitleButton(int ldrawableId, OnClickListener listener);
    
    /**
     * 设置右标题 <BR>
     * @param rdrawableId 右标题图片
     * @param listener 右标题事件
     */
    void setRightButtonFirst(int rdrawableId, OnClickListener listener);
    
    /**
     * 设置右面1按钮是否可见<BR>
     * @param visible 是否可见
     */
    void setRightButtonFirstVisible(boolean visible);
    
    /**
     * 设置右标题 <BR>
     * @param rdrawableId 右标题图片
     * @param listener 右标题事件
     */
    void setRightButtonSecond(int rdrawableId, OnClickListener listener);
    
    /**
     * 设置右面2按钮是否可见<BR>
     * @param visible 是否可见
     */
    void setRightButtonSecondVisible(boolean visible);
    
    /**
     * 
     * 设置中间的标题 <BR>
     * @param titleId 文字资源ID
     */
    void setMiddleTitle(int titleId);
    
    /**
     * 设置中间的标题 <BR>
     * @param titleStr 文字字符串
     */
    void setMiddleTitle(CharSequence titleStr);
    
    /**
     * 设置标题图片资源<BR>
     * @param drawableId 图片资源ID
     */
    void setMiddleTitleDrawable(int drawableId);
    
    /**
     * 设置标题<BR>
     * @param texttitle 中间
     * @param ldrawableId 左边图片
     * @param rdrawableIdFirst 右边第一个button图片
     * @param rdrawableIdSecond 右边第二个button图片
     */
    void setTitle(int texttitle, int ldrawableId, int rdrawableIdFirst,
            int rdrawableIdSecond);
    
    /**
     * 设置titlebar的总页数<BR>
     * @param count 总页数
     */
    void setPageCount(int count);
    
    /**
     * 设置titleBar的当前页<BR>
     * @param currentPage 当前页
     */
    void setCurrentPage(int currentPage);
    
    /**
     * 设置是否显示数字 currentPage\totalCount 例如 1\5<BR>
     * @param show 是否显示
     */
    void showPageNumberView(boolean show);
    
    /**
     * 是否显示指示器<BR>
     * @param show 是否显示
     */
    void showPageIndicatorView(boolean show);
    
    /**
     * 初始化titleBar<BR>
     * 子类需要在该方法中调用设置按钮标题的方法，并且需要返回true才会显示标题栏
     * @return 是否显示titleBar
     */
    boolean initializeTitlBar();
    
    /**
     * 获得标题栏类型，子类根据需要重写此方法展示不同样式标题栏<BR>
     * @return 标题栏类型
     */
    TitleBarStyle getTitleBarStyle();
    
    /**
     * 设置内容页的背景<BR>
     * @return 背景
     */
    int getContentContainerBgId();
    
    /**
     * 设置标题栏背景<BR>
     * @param backgroundId 背景ID
     */
    void setTitleBarBackground(int backgroundId);
    
    /**
     * 
     * 设置标题栏是否可见<BR>
     * @param isVisible 是否可见
     */
    void setTitleBarVisible(boolean isVisible);
    
    /**
     * 
     * 获取标题是否可见<BR>
     * @return 返回标题是否可见
     */
    boolean getTitleBarVisible();
    
}
