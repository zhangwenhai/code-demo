/*
 * 文件名: HorizontalProgressDialog.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 水平进度条类型的dailog
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.ui.basic.dialog;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.R;

/**
 * 水平进度条类型的dailog<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class HorizontalProgressDialog
{
    /**
     * progressdialog实例
     */
    private BasicDialog mProgressDialog = null;

    /**
     * 构造函数
     * 创建一个basicdialog实例
     * @param context context
     */
    public HorizontalProgressDialog(Context context)
    {
        this(context, R.style.Translucent_NoTitle);
    }

    /**
     * 构造函数
     * 创建一个basicdialog实例
     * @param context context
     * @param theme 主题
     */
    public HorizontalProgressDialog(Context context, int theme)
    {
        mProgressDialog = new BasicDialog(context, theme);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_horizontal_progress, null);
        mProgressDialog.setCustomContentView(view);
    }
    
    /**
     * 设置title<BR>
     * @param titleText 标题文字
     * @return progressdialog实例
     */
    public HorizontalProgressDialog setTitle(CharSequence titleText)
    {
        mProgressDialog.setTitle(titleText);
        return this;
    }
    
    /**
     * 设置button<BR>
     * @param whichButton 
     *               DialogInterface.BUTTON_POSITIVE 左按钮
     *               DialogInterface.BUTTON_NEGATIVE 右按钮
     * @param text  button上显示的文字
     * @param listener  button监听
     * @return progressdialog实例
     */
    public HorizontalProgressDialog setButton(int whichButton,
            CharSequence text, OnClickListener listener)
    {
        mProgressDialog.setButton(whichButton, text, listener);
        return this;
    }
    
    /**
     * 设置需要显示的消息<BR>
     * @param percentText 需要显示的百分比提示消息
     */
    public void setPercentText(CharSequence percentText)
    {
        TextView percentTextView = (TextView) mProgressDialog.getCustomContentView()
                .findViewById(R.id.progress_percent);
        
        if (null != percentTextView)
        {
            percentTextView.setText(percentText);
        }
    }
    
    /**
     * 设置需要显示的消息<BR>
     * @param numText 需要显示的百分比提示消息
     */
    public void setNumText(CharSequence numText)
    {
        TextView numTextView = (TextView) mProgressDialog.getCustomContentView()
                .findViewById(R.id.progress_percent);
        
        if (null != numTextView)
        {
            numTextView.setText(numText);
        }
    }
    
    /**
     * 设置进度<BR>
     * @param value 进度值
     */
    public void setProgress(int value)
    {
        ProgressBar progressBar = (ProgressBar) mProgressDialog.getCustomContentView()
                .findViewById(R.id.progressbar);
        progressBar.setProgress(value);
    }
    
    /**
     * showdialog<BR>
     */
    public void show()
    {
        mProgressDialog.show();
    }
    
    /**
     * 关闭dialog<BR>
     */
    public void dismiss()
    {
        mProgressDialog.dismiss();
    }
    
    /**
     * dialog是否在显示<BR>
     * @return true 在显示 ；false 不在显示
     */
    public boolean isShowing()
    {
        return mProgressDialog.isShowing();
    }
}
