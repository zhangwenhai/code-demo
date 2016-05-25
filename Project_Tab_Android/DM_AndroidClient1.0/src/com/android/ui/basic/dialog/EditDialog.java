/*
 * 文件名: EditDialog.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 编辑框类型的Dialog
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
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.android.R;

/**
 * 编辑框类型的Dialog<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class EditDialog
{
    
    /**
     * editdialog 实例
     */
    private BasicDialog mEditDialog = null;

    /**
     * EditText 实例
     */
    private EditText mEditText = null;

    /**
     * 构造器
     * @param context Context对象
     */
    public EditDialog(Context context)
    {
        this(context, R.style.Translucent_NoTitle);
    }

    /**
     * 构造函数
     * 创建一个basicdialog实例
     * @param context context
     * @param theme 主题
     */
    public EditDialog(Context context, int theme)
    {
        mEditDialog = new BasicDialog(context, theme);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_edit, null);
        mEditText = (EditText) view.findViewById(R.id.dialog_input_edit);
        mEditDialog.setCustomContentView(view);
        mEditDialog.getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /**
     * 设置输入框的默认文本<BR>
     * @param hintText 默认文本内容
     * @return EditDialog实例
     */
    public EditDialog setEditHint(CharSequence hintText)
    {
        mEditText.setHint(hintText);
        return this;
    }

    /**
     * 设置Negative Button的内容和监听<BR>
     * @param text 按钮的文本内容
     * @param listener 按钮的事件监听
     * @return EditDialog实例
     */
    public EditDialog setNegativeButton(CharSequence text,
            OnClickListener listener)
    {
        mEditDialog.setButton(BasicDialog.BUTTON_NEGATIVE, text, listener);
        return this;
    }

    /**
     * 设置Positive Button的内容和监听<BR>
     * @param text 按钮的文本内容
     * @param listener 按钮的事件监听
     * @return EditDialog实例
     */
    public EditDialog setPositiveButton(CharSequence text,
            OnClickListener listener)
    {
        mEditDialog.setButton(BasicDialog.BUTTON_POSITIVE, text, listener);
        return this;
    }

    /**
     * 设置输入最大长度<BR>
     * @param maxleight 设置输入最大长度
     * @return EditDialog实例
     */
    public EditDialog setMaxLeight(int maxleight)
    {
        mEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
                maxleight) });
        return this;
    }

    /**
     * 设置标题<BR>
     * @param title 标题
     * @return  EditDialog实例
     * @see android.app.Dialog#setTitle(CharSequence)
     */
    public EditDialog setTitle(CharSequence title)
    {
        mEditDialog.setTitle(title);
        return this;
    }

    public String getEditContent()
    {
        return mEditText.getText().toString();
    }

    /**
     * 设置输入框文字<BR>
     * @param content 文字内容
     */
    public void setEditContent(CharSequence content)
    {
        mEditText.setText(content);
    }

    /**
     * showdialog<BR>
     */
    public void show()
    {
        mEditDialog.show();
    }

    /**
     * 关闭dialog<BR>
     */
    public void dismiss()
    {
        mEditDialog.dismiss();
    }

    /**
     * dialog是否在显示<BR>
     * @return true 在显示 ；false 不在显示
     */
    public boolean isShowing()
    {
        return mEditDialog.isShowing();
    }

    /**
     * 获得basicDilog实例<BR>
     * @return basicDialog实例
     */
    public BasicDialog getDialog()
    {
        return mEditDialog;
    }
    
}
