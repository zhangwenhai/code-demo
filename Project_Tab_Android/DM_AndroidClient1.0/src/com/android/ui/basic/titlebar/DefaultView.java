/*
 * 文件名: DefaultView.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 当未登录的时候展示的页面
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.R;

/**
 * 当未登录的时候展示的页面<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class DefaultView extends LinearLayout
{
    private TextView mLoginTextView;
    
    /**
     * 构造方法
     * @param context 上下文
     */
    public DefaultView(Context context)
    {
        super(context);
        initView();
    }
    
    /**
     * 构造方法
     * @param context 上下文
     * @param attrs 属性
     */
    public DefaultView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView();
    }
    
    private void initView()
    {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.content_fragment_view_not_login, null);
        mLoginTextView = (TextView) view.findViewById(R.id.default_view_text_login);
        mLoginTextView.getPaint().setUnderlineText(true);
        addView(view);
    }
    
    /**
     * 设置点击事件监听<BR>
     * @param listener 事件监听
     */
    public void setLinkClickListener(OnClickListener listener)
    {
        mLoginTextView.setOnClickListener(listener);
    }
    
}
