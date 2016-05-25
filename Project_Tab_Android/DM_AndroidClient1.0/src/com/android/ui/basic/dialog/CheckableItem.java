/*
 * 文件名: CheckableItem.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 实现了chekable接口的LinearLayout
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.ui.basic.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.LinearLayout;

import com.android.component.log.Logger;

/**
 * 实现了chekable接口的LinearLayout<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class CheckableItem extends LinearLayout implements Checkable
{
    /**
     * Debug Tag
     */
    private static final String TAG = "CheckableItem";
    
    /**
     * 实现了checkable接口的子视图
     */
    private Checkable mCheckableChild;
    
    /**
     * 构造函数
     * @param context context
     * @param attrs attrs
     */
    public CheckableItem(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    /**
     * 重写addView找到子视图中的checkable视图<BR>
     * @param view view
     * @param params params
     * @see ViewGroup#addView(View, ViewGroup.LayoutParams)
     */
    public void addView(View view, ViewGroup.LayoutParams params)
    {
        super.addView(view, params);
        if (view instanceof Checkable)
        {
            mCheckableChild = (Checkable) view;
        }
    }

    /**
     * 设置选中状态<BR>
     * @param checked ture 被选中
     *                false 取消选中
     * @see Checkable#setChecked(boolean)
     */
    @Override
    public void setChecked(boolean checked)
    {
        if (null != mCheckableChild)
        {
            mCheckableChild.setChecked(checked);
        }
        else
        {
            Logger.d(TAG, "没有checkable对象");
        }
    }

    /**
     * 是否被选中<BR>
     * @return ture 被选中
     *         false 取消选中
     * @see Checkable#isChecked()
     */
    @Override
    public boolean isChecked()
    {
        if (null != mCheckableChild)
        {
            return mCheckableChild.isChecked();
        }
        Logger.d(TAG, "没有checkable对象");
        return false;
    }

    /**
     * 切换选中状态<BR>
     * @see Checkable#toggle()
     */
    @Override
    public void toggle()
    {
        if (null != mCheckableChild)
        {
            mCheckableChild.toggle();
        }
        else
        {
            Logger.d(TAG, "没有checkable对象");
        }
    }
    
}
