/*
 * 文件名: RichMultiChoiceDialog.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 支持给每个list条目指定主标题(必选),副标题的“多选列表型dialog”
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.android.R;

/**
 * 支持给每个list条目指定主标题(必选),副标题的“多选列表型dialog”<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class RichMultiChoiceDialog
{
    /**
     * 上下文
     */
    private Context mContext;
    
    /**
     * BasicDialog实例
     */
    private BasicDialog mDialog = null;

    /**
     * builder
     */
    private BasicDialog.Builder mBuilder;

    /**
     * RichMultiChoiceDialog
     * @param context 上下文
     */
    public RichMultiChoiceDialog(Context context)
    {
        mContext = context;
        mBuilder = new BasicDialog.Builder(mContext);
    }
    
    /**
     * 设置标题<BR>
     * @param title 标题
     * @return RichMultiChoiceDialog实例
     */
    public RichMultiChoiceDialog setTitle(CharSequence title)
    {
        mBuilder.setTitle(title);
        return this;
    }
    
    /**
     * 设置条目<BR>
     * @param itemTitles 条目主标题
     * @param itemSubTitles 条目副标题
     * @param checkedItems 默认选中的标题
     * @param onClickListener 条目的点击监听
     * @return RichMultiChoiceDialog实例
     */
    public RichMultiChoiceDialog setItems(final CharSequence[] itemTitles,
            final CharSequence[] itemSubTitles, final boolean[] checkedItems,
            final DialogInterface.OnMultiChoiceClickListener onClickListener)
    {
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ListView listView = (ListView) inflater.inflate(R.layout.dialog_listview,
                null);
        ListAdapter adapter = new BaseAdapter()
        {
            @Override
            public int getCount()
            {
                return itemTitles.length;
            }
            
            @Override
            public Object getItem(int position)
            {
                return itemTitles[position];
            }
            
            @Override
            public long getItemId(int position)
            {
                return position;
            }
            
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                convertView = inflater.inflate(R.layout.dialog_multi_choice,
                        null);
                //主标题，必须有主标题
                TextView tv = (TextView) convertView.findViewById(R.id.item_text);
                tv.setText(itemTitles[position]);
                //副标题
                if (null != itemSubTitles)
                {
                    TextView subTv = (TextView) convertView.findViewById(R.id.item_sub_text);
                    subTv.setVisibility(View.VISIBLE);
                    subTv.setText(itemSubTitles[position]);
                }
                if (null != checkedItems)
                {
                    boolean isItemChecked = checkedItems[position];
                    if (isItemChecked)
                    {
                        listView.setItemChecked(position, true);
                    }
                }
                return convertView;
            }
        };
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(new OnItemClickListener()
        {
            @SuppressWarnings("rawtypes")
            public void onItemClick(AdapterView parent, View v, int position,
                    long id)
            {
                if (null != checkedItems)
                {
                    checkedItems[position] = listView.isItemChecked(position);
                }
                if (null != onClickListener)
                {
                    onClickListener.onClick(mDialog,
                            position,
                            listView.isItemChecked(position));
                }
            }
        });
        mBuilder.setContentView(listView);
        return this;
    }
    
    /**
     * 设置PositiveButton<BR>
     * @param positiveButtonText positiveButton文字
     * @param listener positiveButton监听
     * @return RichMultiChoiceDialog实例
     */
    public RichMultiChoiceDialog setPositiveButton(String positiveButtonText,
            DialogInterface.OnClickListener listener)
    {
        mBuilder.setPositiveButton(positiveButtonText, listener);
        return this;
    }
    
    /**
     * 设置NegativeButton<BR>
     * @param negativeButtonText negativeButton文字
     * @param listener negativeButton的监听
     * @return RichMultiChoiceDialog实例
     */
    public RichMultiChoiceDialog setNegativeButton(String negativeButtonText,
            DialogInterface.OnClickListener listener)
    {
        mBuilder.setNegativeButton(negativeButtonText, listener);
        return this;
    }
    
    /**
     * 构建BasicDialog<BR>
     * @return RichMultiChoiceDialog实例
     */
    public RichMultiChoiceDialog create()
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
    
    /**
     * 获取dialog上的button<BR>
     * @param whichButton 
     *               DialogInterface.BUTTON_POSITIVE 左按钮
     *               DialogInterface.BUTTON_NEGATIVE 右按钮
     * @return button
     */
    public Button getButton(int whichButton)
    {
        return mDialog.getButton(whichButton);
    }
}
