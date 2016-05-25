/*
 * 文件名: HighlightMsgDialog.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 显示消息的dialog,支持高亮消息体中的某段指定文字
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.ui.basic.dialog;

import android.content.Context;
import android.content.DialogInterface;

import com.android.R;

/**
 * 显示消息的dialog,支持高亮消息体中的某段指定文字<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class HighlightMsgDialog
{
    /**
     * 上下文
     */
    private Context mContext;
    
    /**
     * HighlightDialog实例
     */
    private BasicDialog mDialog = null;

    /**
     * dialog builder
     */
    private BasicDialog.Builder mBuilder;

    /**
     * 处理高亮的PrefixHighlighter实例
     */
    private PrefixHighlighter mPrefixHighlighter;

    /**
     * 构造函数
     * 创建一个basicdialog实例
     * @param context context
     */
    public HighlightMsgDialog(Context context)
    {
        mContext = context;
        mBuilder = new BasicDialog.Builder(context);
        mPrefixHighlighter = new PrefixHighlighter(mContext.getResources()
                .getColor(R.color.dialog_color_holo));
    }
    
    /**
     * 设置dialog标题<BR>
     * @param title 标题资源文件
     * @return HighlightMsgDialog实例
     */
    public HighlightMsgDialog setTitle(int title)
    {
        mBuilder.setTitle(title);
        return this;
    }
    
    /**
     * 设置显示的消息<BR>
     * @param message 消息体
     * @param highlightMessage 消息体中需要高亮的部分
     * @return HighlightMsgDialog实例
     */
    public HighlightMsgDialog setMessage(CharSequence message,
            String highlightMessage)
    {
        message = mPrefixHighlighter.apply(message,
                highlightMessage.toUpperCase().toCharArray());
        mBuilder.setMessage(message);
        return this;
    }
    
    /**
     * 设置positiveButton<BR>
     * @param positiveButtonText Positive按钮文字资源文件
     * @param listener Positive按钮监听
     * @return HighlightMsgDialog实例
     */
    public HighlightMsgDialog setPositiveButton(int positiveButtonText,
            DialogInterface.OnClickListener listener)
    {
        mBuilder.setPositiveButton(positiveButtonText, listener);
        return this;
    }
    
    /**
     * 设置negativeButton<BR>
     * @param negativeButtonText negative按钮文字资源文件
     * @param listener negative按钮监听
     * @return HighlightMsgDialog实例
     */
    public HighlightMsgDialog setNegativeButton(int negativeButtonText,
            DialogInterface.OnClickListener listener)
    {
        mBuilder.setNegativeButton(negativeButtonText, listener);
        return this;
    }
    
    /**
     * 封装BasicDialog.Builder的创建方法<BR>
     * @return HighlightMsgDialog实例
     */
    public HighlightMsgDialog create()
    {
        mDialog = mBuilder.create();
        return this;
    }
    
    /**
     * showdialog<BR>
     */
    public void show()
    {
        mDialog.show();
    }
    
    /**
     * 关闭dialog<BR>
     */
    public void dismiss()
    {
        mDialog.dismiss();
    }
    
    /**
     * dialog是否在显示<BR>
     * @return true 在显示 ；false 不在显示
     */
    public boolean isShowing()
    {
        return mDialog.isShowing();
    }
}
